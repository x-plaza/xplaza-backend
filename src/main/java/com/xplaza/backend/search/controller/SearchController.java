/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.search.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
      @RequestParam(name = "size", defaultValue = "20") int size) {
    var service = searchProvider.getIfAvailable();
    if (service == null) {
      return ResponseEntity.ok(ApiResponse.ok(List.of()));
    }
    var hits = service.search(q, Map.of(), page, size);
    var docs = hits.stream().map(h -> h.getContent()).toList();
    return ResponseEntity.ok(ApiResponse.ok(docs));
  }

  @GetMapping("/autocomplete")
  public ResponseEntity<ApiResponse<List<String>>> autocomplete(
      @RequestParam("q") String q,
      @RequestParam(name = "limit", defaultValue = "8") int limit) {
    var service = searchProvider.getIfAvailable();
    if (service == null) return ResponseEntity.ok(ApiResponse.ok(List.of()));
    return ResponseEntity.ok(ApiResponse.ok(service.autocomplete(q, limit)));
  }
}
