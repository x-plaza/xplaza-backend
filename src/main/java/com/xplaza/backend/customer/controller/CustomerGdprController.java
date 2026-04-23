/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.customer.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.customer.domain.entity.Customer;
import com.xplaza.backend.customer.dto.response.CustomerProfileResponse;
import com.xplaza.backend.customer.service.CustomerService;

@RestController
@RequestMapping("/api/v1/customer/gdpr")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerGdprController {

  private final CustomerService customerService;

  @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse<CustomerProfileResponse>> export(@AuthenticationPrincipal Customer customer) {
    Customer data = customerService.exportCustomerData(customer.getCustomerId());
    return ResponseEntity.ok(ApiResponse.ok(CustomerProfileResponse.from(data)));
  }

  @DeleteMapping
  public ResponseEntity<ApiResponse<Void>> erase(@AuthenticationPrincipal Customer customer) {
    customerService.eraseCustomerData(customer.getCustomerId());
    return ResponseEntity.ok(ApiResponse.ok("Customer data erased"));
  }
}
