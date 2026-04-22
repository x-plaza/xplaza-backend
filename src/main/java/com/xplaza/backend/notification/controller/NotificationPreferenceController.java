/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.notification.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.customer.domain.entity.Customer;
import com.xplaza.backend.notification.domain.entity.NotificationPreference;
import com.xplaza.backend.notification.domain.repository.NotificationPreferenceRepository;

@RestController
@RequestMapping("/api/v1/customer/notification-preferences")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class NotificationPreferenceController {

  private final NotificationPreferenceRepository repo;

  @GetMapping
  public ResponseEntity<ApiResponse<NotificationPreference>> get(@AuthenticationPrincipal Customer customer) {
    var pref = repo.findById(customer.getCustomerId())
        .orElseGet(() -> NotificationPreference.builder().customerId(customer.getCustomerId()).build());
    return ResponseEntity.ok(ApiResponse.ok(pref));
  }

  @PutMapping
  public ResponseEntity<ApiResponse<NotificationPreference>> update(@AuthenticationPrincipal Customer customer,
      @RequestBody NotificationPreference body) {
    body.setCustomerId(customer.getCustomerId());
    return ResponseEntity.ok(ApiResponse.ok(repo.save(body)));
  }
}
