/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.loyalty.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.customer.domain.entity.Customer;
import com.xplaza.backend.customer.domain.repository.CustomerRepository;

@RestController
@RequestMapping("/api/v1/customer/loyalty")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class LoyaltyController {

  private final CustomerRepository customerRepository;

  public record LoyaltyBalance(long points, String tier) {}

  @GetMapping("/balance")
  public ResponseEntity<ApiResponse<LoyaltyBalance>> balance(@AuthenticationPrincipal Customer customer) {
    var c = customerRepository.findById(customer.getCustomerId()).orElseThrow();
    long points = c.getLoyaltyPoints() == null ? 0L : c.getLoyaltyPoints();
    String tier = c.getLoyaltyTier() == null ? "BRONZE" : c.getLoyaltyTier();
    return ResponseEntity.ok(ApiResponse.ok(new LoyaltyBalance(points, tier)));
  }
}
