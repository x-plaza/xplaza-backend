/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.payment.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.customer.domain.entity.Customer;
import com.xplaza.backend.payment.domain.entity.CustomerPaymentMethod;
import com.xplaza.backend.payment.service.SavedPaymentMethodService;

@RestController
@RequestMapping("/api/v1/customer/payment-methods")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class SavedPaymentMethodController {

  private final SavedPaymentMethodService service;

  @GetMapping
  public ResponseEntity<ApiResponse<List<CustomerPaymentMethod>>> list(@AuthenticationPrincipal Customer customer) {
    return ResponseEntity.ok(ApiResponse.ok(service.list(customer.getCustomerId())));
  }

  @PostMapping
  public ResponseEntity<ApiResponse<CustomerPaymentMethod>> create(@AuthenticationPrincipal Customer customer,
      @Valid @RequestBody CustomerPaymentMethod input) {
    return ResponseEntity.ok(ApiResponse.ok(service.vault(customer.getCustomerId(), input)));
  }

  @PostMapping("/{id}/default")
  public ResponseEntity<ApiResponse<CustomerPaymentMethod>> setDefault(@AuthenticationPrincipal Customer customer,
      @PathVariable UUID id) {
    return ResponseEntity.ok(ApiResponse.ok(service.setDefault(customer.getCustomerId(), id)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal Customer customer,
      @PathVariable UUID id) {
    service.remove(customer.getCustomerId(), id);
    return ResponseEntity.ok(ApiResponse.ok("Payment method removed"));
  }
}
