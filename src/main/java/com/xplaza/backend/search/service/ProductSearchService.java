/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.search.service;

import java.time.Instant;
import java.util.*;

import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
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
  public void reindex(Long productId) {
    productRepository.findById(productId).ifPresent(p -> documentRepository.save(toDocument(p)));
  }

  public void delete(Long productId) {
    documentRepository.deleteById(String.valueOf(productId));
  }

  public SearchHits<ProductDocument> search(String q, Map<String, String> filters, int page, int size) {
    var nativeQueryBuilder = NativeQuery.builder()
        .withPageable(PageRequest.of(page, size));
    if (q != null && !q.isBlank()) {
      var mm = MultiMatchQuery.of(m -> m
          .query(q)
          .fields("name^3", "nameSuggest^2", "description", "brand", "category", "tags")
          .operator(Operator.And)
          .fuzziness("AUTO"))._toQuery();
      nativeQueryBuilder.withQuery(mm);
    }
    Query query = nativeQueryBuilder.build();
    return operations.search(query, ProductDocument.class);
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
