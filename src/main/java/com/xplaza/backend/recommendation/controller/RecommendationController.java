/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.recommendation.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.catalog.domain.entity.Product;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.customer.domain.entity.Customer;
import com.xplaza.backend.recommendation.service.RecommendationService;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

  private final RecommendationService service;

  @GetMapping("/products/{productId}/related")
  public ResponseEntity<ApiResponse<List<Product>>> related(@PathVariable Long productId,
      @RequestParam(defaultValue = "10") int limit) {
    return ResponseEntity.ok(ApiResponse.ok(service.related(productId, limit)));
  }

  @GetMapping("/products/{productId}/frequently-bought-together")
  public ResponseEntity<ApiResponse<List<Product>>> fbt(@PathVariable Long productId,
      @RequestParam(defaultValue = "5") int limit) {
    return ResponseEntity.ok(ApiResponse.ok(service.frequentlyBoughtTogether(productId, limit)));
  }

  @GetMapping("/recently-viewed")
  public ResponseEntity<ApiResponse<List<Product>>> recentlyViewed(@AuthenticationPrincipal Customer customer,
      @RequestParam(defaultValue = "10") int limit) {
    if (customer == null)
      return ResponseEntity.ok(ApiResponse.ok(List.of()));
    return ResponseEntity.ok(ApiResponse.ok(service.recentlyViewed(customer.getCustomerId(), limit)));
  }

  @PostMapping("/products/{productId}/views")
  public ResponseEntity<ApiResponse<Void>> recordView(@AuthenticationPrincipal Customer customer,
      @PathVariable Long productId) {
    if (customer != null)
      service.recordView(customer.getCustomerId(), productId);
    return ResponseEntity.ok(ApiResponse.ok("recorded"));
  }
}
