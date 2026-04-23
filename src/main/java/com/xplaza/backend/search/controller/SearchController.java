/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.search.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.search.document.ProductDocument;
import com.xplaza.backend.search.service.ProductSearchService;

/**
 * Public search endpoints. The underlying {@link ProductSearchService} is
 * optional (only registered when Elasticsearch is enabled), so the controller
 * uses {@link ObjectProvider} and returns a 503-style payload when the service
 * is unavailable.
 */
@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

  private final ObjectProvider<ProductSearchService> searchProvider;

  public SearchController(ObjectProvider<ProductSearchService> searchProvider) {
    this.searchProvider = searchProvider;
  }

  @GetMapping("/products")
  public ResponseEntity<ApiResponse<List<ProductDocument>>> products(
      @RequestParam(name = "q", required = false) String q,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "20") int size,
      @RequestParam Map<String, String> all) {
    var service = searchProvider.getIfAvailable();
    if (service == null) {
      return ResponseEntity.ok(ApiResponse.ok(List.of()));
    }
    var filters = stripPagingParams(all);
    var hits = service.search(q, filters, page, size);
    var docs = hits.stream().map(h -> h.getContent()).toList();
    return ResponseEntity.ok(ApiResponse.ok(docs));
  }

  /**
   * Faceted search returning hits + aggregations (brand / category / price
   * histogram). Storefronts use this to render filter sidebars.
   */
  @GetMapping("/products/faceted")
  public ResponseEntity<ApiResponse<Map<String, Object>>> faceted(
      @RequestParam(name = "q", required = false) String q,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "20") int size,
      @RequestParam Map<String, String> all) {
    var service = searchProvider.getIfAvailable();
    if (service == null) {
      return ResponseEntity.ok(ApiResponse.ok(Map.of("hits", List.of(), "facets", Map.of())));
    }
    var filters = stripPagingParams(all);
    var hits = service.search(q, filters, page, size, true);
    Map<String, Object> body = new HashMap<>();
    body.put("hits", hits.stream().map(h -> h.getContent()).toList());
    body.put("total", hits.getTotalHits());
    body.put("aggregations", hits.getAggregations());
    return ResponseEntity.ok(ApiResponse.ok(body));
  }

  /**
   * Admin-only bulk reindex. Walks the products table in batches and pushes every
   * row to Elasticsearch. Returns the number of documents indexed.
   */
  @PostMapping("/reindex")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<Map<String, Object>>> reindex(
      @RequestParam(name = "batchSize", defaultValue = "500") int batchSize) {
    var service = searchProvider.getIfAvailable();
    if (service == null) {
      return ResponseEntity.status(503).body(ApiResponse.ok(Map.of("status", "search-disabled")));
    }
    int total = service.reindexAll(batchSize);
    return ResponseEntity.ok(ApiResponse.ok(Map.of("status", "ok", "indexed", total)));
  }

  private static Map<String, String> stripPagingParams(Map<String, String> all) {
    Map<String, String> filters = new HashMap<>(all);
    filters.remove("q");
    filters.remove("page");
    filters.remove("size");
    return filters;
  }

  @GetMapping("/autocomplete")
  public ResponseEntity<ApiResponse<List<String>>> autocomplete(
      @RequestParam("q") String q,
      @RequestParam(name = "limit", defaultValue = "8") int limit) {
    var service = searchProvider.getIfAvailable();
    if (service == null) {
      return ResponseEntity.ok(ApiResponse.ok(List.of()));
    }
    return ResponseEntity.ok(ApiResponse.ok(service.autocomplete(q, limit)));
  }
}
