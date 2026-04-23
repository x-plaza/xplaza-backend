/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.marketing.controller;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.customer.domain.entity.Customer;
import com.xplaza.backend.marketing.domain.entity.Referral;
import com.xplaza.backend.marketing.service.ReferralService;

@RestController
@RequestMapping("/api/v1/customer/referrals")
@RequiredArgsConstructor
@Tag(name = "Referrals", description = "Customer referral program")
public class ReferralController {

  private final ReferralService referralService;

  @Operation(summary = "Send a referral invitation")
  @PostMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<Referral> createReferral(
      @AuthenticationPrincipal Customer principal,
      @RequestBody @org.springframework.validation.annotation.Validated CreateReferralRequest request) {
    Referral referral = referralService.createReferral(principal.getCustomerId(), request.email());
    return ResponseEntity.ok(referral);
  }

  @Operation(summary = "List my referrals")
  @GetMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<List<Referral>> listReferrals(@AuthenticationPrincipal Customer principal) {
    return ResponseEntity.ok(referralService.listReferrals(principal.getCustomerId()));
  }

  @Operation(summary = "Mark a referral code as accepted by the current customer")
  @PostMapping("/accept/{code}")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<Void> accept(@AuthenticationPrincipal Customer principal,
      @PathVariable String code) {
    referralService.markAccepted(code, principal.getCustomerId());
    return ResponseEntity.noContent().build();
  }

  public record CreateReferralRequest(@NotNull @NotBlank @Email String email) {
  }
}
