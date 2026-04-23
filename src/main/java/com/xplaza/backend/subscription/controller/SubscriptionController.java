/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.subscription.controller;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.customer.domain.entity.Customer;
import com.xplaza.backend.subscription.domain.entity.Subscription;
import com.xplaza.backend.subscription.domain.entity.SubscriptionItem;
import com.xplaza.backend.subscription.service.SubscriptionService;

@RestController
@RequestMapping("/api/v1/customer/subscriptions")
@RequiredArgsConstructor
@Tag(name = "Subscriptions", description = "Customer subscription management")
public class SubscriptionController {

  private final SubscriptionService subscriptionService;

  @Operation(summary = "Create a subscription")
  @PostMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<Subscription> create(@AuthenticationPrincipal Customer principal,
      @RequestBody @Valid CreateSubscriptionRequest request) {
    var items = request.items().stream()
        .map(it -> SubscriptionItem.builder()
            .productId(it.productId())
            .quantity(it.quantity())
            .unitPrice(it.unitPrice())
            .build())
        .toList();
    var sub = subscriptionService.create(principal.getCustomerId(), request.intervalUnit(),
        request.intervalCount(), request.currency(), items);
    return ResponseEntity.ok(sub);
  }

  @Operation(summary = "List my subscriptions")
  @GetMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<List<Subscription>> list(@AuthenticationPrincipal Customer principal) {
    return ResponseEntity.ok(subscriptionService.listForCustomer(principal.getCustomerId()));
  }

  @Operation(summary = "Pause a subscription")
  @PostMapping("/{id}/pause")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<Subscription> pause(@AuthenticationPrincipal Customer principal,
      @PathVariable Long id) {
    requireOwnership(principal, id);
    return ResponseEntity.ok(subscriptionService.pause(id));
  }

  @Operation(summary = "Resume a subscription")
  @PostMapping("/{id}/resume")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<Subscription> resume(@AuthenticationPrincipal Customer principal,
      @PathVariable Long id) {
    requireOwnership(principal, id);
    return ResponseEntity.ok(subscriptionService.resume(id));
  }

  @Operation(summary = "Cancel a subscription")
  @PostMapping("/{id}/cancel")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<Subscription> cancel(@AuthenticationPrincipal Customer principal,
      @PathVariable Long id) {
    requireOwnership(principal, id);
    return ResponseEntity.ok(subscriptionService.cancel(id));
  }

  private void requireOwnership(Customer principal, Long subscriptionId) {
    var s = subscriptionService.get(subscriptionId);
    if (!principal.getCustomerId().equals(s.getCustomerId())) {
      throw new org.springframework.security.access.AccessDeniedException(
          "Subscription does not belong to current customer");
    }
  }

  public record CreateSubscriptionRequest(
      @NotNull Subscription.IntervalUnit intervalUnit,
      @NotNull @Positive Integer intervalCount,
      @NotNull String currency,
      @NotEmpty List<@Valid Item> items
  ) {
  }

  public record Item(
      @NotNull Long productId,
      @NotNull @Positive Integer quantity,
      @NotNull BigDecimal unitPrice
  ) {
  }
}
