/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.search.service;

import java.time.Instant;
import java.util.*;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.xplaza.backend.catalog.domain.entity.Product;
import com.xplaza.backend.catalog.domain.repository.ProductRepository;
import com.xplaza.backend.common.events.DomainEvents;
import com.xplaza.backend.search.document.ProductDocument;
import com.xplaza.backend.search.repository.ProductDocumentRepository;

/**
 * Search facade backed by Elasticsearch. The bean is only registered when
 * {@code search.elasticsearch.enabled=true} so the application can run in
 * environments without a search cluster (e.g. CI smoke tests).
 */
@Service
@ConditionalOnProperty(prefix = "search.elasticsearch", name = "enabled", havingValue = "true", matchIfMissing = false)
@Slf4j
public class ProductSearchService {

  private final ProductDocumentRepository documentRepository;
  private final ElasticsearchOperations operations;
  private final ProductRepository productRepository;

  @Autowired
  public ProductSearchService(ProductDocumentRepository documentRepository,
      ElasticsearchOperations operations,
      ProductRepository productRepository) {
    this.documentRepository = documentRepository;
    this.operations = operations;
    this.productRepository = productRepository;
  }

  /** Reindex a single product. Called from event listener. */
  @Async
  @CircuitBreaker(name = "elasticsearch")
  @Retry(name = "elasticsearch")
  public void reindex(Long productId) {
    productRepository.findById(productId).ifPresent(p -> documentRepository.save(toDocument(p)));
  }

  public void delete(Long productId) {
    documentRepository.deleteById(String.valueOf(productId));
  }

  public SearchHits<ProductDocument> search(String q, Map<String, String> filters, int page, int size) {
    return search(q, filters, page, size, false);
  }

  /**
   * Full-text search + structured filters + (optional) facet aggregations.
   * Filters honoured: shopId, brand, category, minPrice, maxPrice, published.
   * When {@code withFacets} is true the response includes term aggregations for
   * brand and category and a histogram for price.
   */
  public SearchHits<ProductDocument> search(String q, Map<String, String> filters, int page, int size,
      boolean withFacets) {
    var bool = BoolQuery.of(b -> {
      if (q != null && !q.isBlank()) {
        b.must(MultiMatchQuery.of(m -> m
            .query(q)
            .fields("name^3", "nameSuggest^2", "description", "brand", "category", "tags")
            .operator(Operator.And)
            .fuzziness("AUTO"))._toQuery());
      }
      if (filters != null) {
        // Numeric and boolean filters must be parsed to their typed FieldValue
        // otherwise Elasticsearch's term query compiles as a string comparison
        // against an indexed long/bool field and silently returns zero hits.
        applyLongTermFilter(b, filters, "shopId", "shopId");
        applyStringTermFilter(b, filters, "brand", "brand");
        applyStringTermFilter(b, filters, "category", "category");
        applyStringTermFilter(b, filters, "currency", "currency");
        applyBooleanTermFilter(b, filters, "published", "published");
        applyPriceRange(b, filters);
      }
      return b;
    });
    var nativeQueryBuilder = NativeQuery.builder()
        .withQuery(bool._toQuery())
        .withPageable(PageRequest.of(page, size));
    if (withFacets) {
      nativeQueryBuilder.withAggregation("brand",
          Aggregation.of(a -> a.terms(t -> t.field("brand").size(50))));
      nativeQueryBuilder.withAggregation("category",
          Aggregation.of(a -> a.terms(t -> t.field("category").size(50))));
      nativeQueryBuilder.withAggregation("price",
          Aggregation.of(a -> a.histogram(h -> h.field("price").interval(50.0))));
    }
    Query query = nativeQueryBuilder.build();
    return operations.search(query, ProductDocument.class);
  }

  private static void applyStringTermFilter(BoolQuery.Builder b, Map<String, String> filters,
      String key, String field) {
    var v = filters.get(key);
    if (v != null && !v.isBlank()) {
      b.filter(TermQuery.of(t -> t.field(field).value(v))._toQuery());
    }
  }

  private static void applyLongTermFilter(BoolQuery.Builder b, Map<String, String> filters,
      String key, String field) {
    var v = filters.get(key);
    if (v == null || v.isBlank()) {
      return;
    }
    try {
      long parsed = Long.parseLong(v.trim());
      b.filter(TermQuery.of(t -> t.field(field).value(parsed))._toQuery());
    } catch (NumberFormatException e) {
      log.debug("Skipping non-numeric filter for '{}' = '{}'", key, v);
    }
  }

  private static void applyBooleanTermFilter(BoolQuery.Builder b, Map<String, String> filters,
      String key, String field) {
    var v = filters.get(key);
    if (v == null || v.isBlank()) {
      return;
    }
    String trimmed = v.trim();
    // Only accept the canonical spellings so we don't silently treat "yes"
    // as false. Anything else is ignored (a filter-not-applied is safer than
    // a wrong filter applied).
    if (trimmed.equalsIgnoreCase("true") || trimmed.equalsIgnoreCase("false")) {
      boolean parsed = Boolean.parseBoolean(trimmed);
      b.filter(TermQuery.of(t -> t.field(field).value(parsed))._toQuery());
    } else {
      log.debug("Skipping non-boolean filter for '{}' = '{}'", key, v);
    }
  }

  private static void applyPriceRange(BoolQuery.Builder b, Map<String, String> filters) {
    var min = parseDouble(filters.get("minPrice"));
    var max = parseDouble(filters.get("maxPrice"));
    if (min == null && max == null) {
      return;
    }
    b.filter(RangeQuery.of(r -> {
      var nb = r.number(n -> {
        var nbb = n.field("price");
        if (min != null) {
          nbb.gte(min);
        }
        if (max != null) {
          nbb.lte(max);
        }
        return nbb;
      });
      return nb;
    })._toQuery());
  }

  private static Double parseDouble(String s) {
    if (s == null || s.isBlank()) {
      return null;
    }
    try {
      return Double.parseDouble(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * Walk the entire products table in batches and rebuild the search index.
   * Intended to be triggered manually (admin endpoint) after schema changes or as
   * a recovery step when a partial outage left the index stale.
   */
  public int reindexAll(int batchSize) {
    int total = 0;
    int page = 0;
    while (true) {
      var slice = productRepository.findAll(PageRequest.of(page, batchSize));
      if (slice.isEmpty()) {
        break;
      }
      var docs = slice.stream().map(this::toDocument).toList();
      documentRepository.saveAll(docs);
      total += docs.size();
      if (!slice.hasNext()) {
        break;
      }
      page++;
    }
    log.info("Bulk reindex completed: {} products", total);
    return total;
  }

  public List<String> autocomplete(String prefix, int limit) {
    if (prefix == null || prefix.isBlank()) {
      return List.of();
    }
    var query = NativeQuery.builder()
        .withQuery(co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery.of(m -> m
            .query(prefix)
            .type(co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType.BoolPrefix)
            .fields("nameSuggest", "nameSuggest._2gram", "nameSuggest._3gram"))._toQuery())
        .withPageable(PageRequest.of(0, limit))
        .build();
    return operations.search(query, ProductDocument.class).stream()
        .map(h -> h.getContent().getName())
        .distinct()
        .toList();
  }

  @EventListener
  public void on(DomainEvents.ProductIndexInvalidated event) {
    reindex(event.productId());
  }

  ProductDocument toDocument(Product p) {
    return ProductDocument.builder()
        .id(String.valueOf(p.getProductId()))
        .productId(p.getProductId())
        .name(p.getProductName())
        .nameSuggest(p.getProductName())
        .description(p.getProductDescription())
        .slug(p.getSlug())
        .brand(p.getBrand() != null ? p.getBrand().getBrandName() : null)
        .category(p.getCategory() != null ? p.getCategory().getCategoryName() : null)
        .tags(List.of())
        .price(p.getProductSellingPrice())
        .currency(p.getCurrency() != null ? p.getCurrency().getCurrencyName() : null)
        .shopId(p.getShop() != null ? p.getShop().getShopId() : null)
        .shopName(p.getShop() != null ? p.getShop().getShopName() : null)
        .quantity(p.getQuantity())
        .published(Boolean.TRUE.equals(p.getIsPublished()))
        .averageRating(p.getAverageRating())
        .reviewCount(p.getReviewCount())
        .attributes(new HashMap<>())
        .indexedAt(Instant.now())
        .build();
  }
}
