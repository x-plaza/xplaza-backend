/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.giftcard.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.giftcard.domain.entity.GiftCard;
import com.xplaza.backend.giftcard.service.GiftCardService;

@RestController
@RequestMapping("/api/v1/gift-cards")
@RequiredArgsConstructor
public class GiftCardController {

  private final GiftCardService service;

  public record IssueRequest(
      BigDecimal amount,
      String currency,
      String email,
      Long customerId,
      LocalDate expiresOn
  ) {
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<GiftCard>> issue(@RequestBody IssueRequest req) {
    return ResponseEntity.ok(ApiResponse.ok(service.issue(req.amount(), req.currency(), req.email(),
        req.customerId(), req.expiresOn())));
  }

  @GetMapping("/{code}/balance")
  public ResponseEntity<ApiResponse<GiftCard>> balance(@PathVariable String code) {
    return ResponseEntity.ok(ApiResponse.ok(service.checkBalance(code)));
  }
}
