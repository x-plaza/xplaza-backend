/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.notification.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.customer.domain.entity.Customer;
import com.xplaza.backend.notification.domain.entity.PushToken;
import com.xplaza.backend.notification.service.PushNotificationService;

/**
 * Customer-facing endpoints for managing FCM/APNs device tokens. Auth is
 * enforced via {@code ROLE_CUSTOMER}; the token is always written against the
 * authenticated principal so a malicious client cannot register a push token
 * for someone else.
 */
@RestController
@RequestMapping("/api/v1/push-tokens")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@Tag(name = "Push Tokens", description = "Register / revoke device push tokens")
public class PushTokenController {

  private final PushNotificationService pushService;

  @Operation(summary = "Register or refresh the current device's push token")
  @PostMapping("/register")
  public ResponseEntity<PushToken> register(
      @RequestParam PushToken.Platform platform,
      @RequestParam @NotBlank String token,
      @RequestParam(required = false) String deviceId) {
    Long customerId = currentCustomerId();
    return ResponseEntity.ok(pushService.registerToken(customerId, platform, token, deviceId));
  }

  @Operation(summary = "Unregister a device push token (e.g. on sign-out)")
  @DeleteMapping
  public ResponseEntity<Map<String, String>> unregister(@RequestParam @NotBlank String token) {
    pushService.unregisterToken(token);
    return ResponseEntity.ok(Map.of("status", "ok"));
  }

  @Operation(summary = "List the authenticated customer's registered tokens")
  @GetMapping
  public ResponseEntity<List<PushToken>> list() {
    return ResponseEntity.ok(pushService.tokensForCustomer(currentCustomerId()));
  }

  private static Long currentCustomerId() {
    Principal p = (Principal) SecurityContextHolder.getContext().getAuthentication();
    if (p instanceof org.springframework.security.core.Authentication auth
        && auth.getPrincipal() instanceof Customer c) {
      return c.getCustomerId();
    }
    throw new IllegalStateException("No authenticated customer");
  }
}
