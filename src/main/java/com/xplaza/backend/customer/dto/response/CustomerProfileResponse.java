/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.customer.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import com.xplaza.backend.customer.domain.entity.Customer;

/**
 * Safe projection of {@link Customer} for endpoints that hand the customer's
 * own profile back to them ({@code /me}, {@code /gdpr/export}, ...).
 *
 * <p>
 * This DTO is the API contract for those endpoints — never return the JPA
 * entity directly. It deliberately omits credential-grade fields
 * ({@code password}, {@code mfaSecret}, {@code oauthSubject}) and
 * security-state fields ({@code failedLoginAttempts}, {@code lockedUntil})
 * because exposing them would let an attacker who hijacks a session also
 * brute-force the password offline, generate valid TOTP codes, or time
 * credential-stuffing windows.
 */
public record CustomerProfileResponse(
    Long customerId,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    String role,
    Boolean enabled,
    Boolean emailVerified,
    Instant emailVerifiedAt,
    Boolean mfaEnabled,
    String oauthProvider,
    Long loyaltyPoints,
    String loyaltyTier,
    BigDecimal storeCredit,
    Long customerGroupId,
    String taxId,
    LocalDateTime createdAt,
    LocalDateTime lastLoginAt
) {

  public static CustomerProfileResponse from(Customer c) {
    if (c == null) {
      return null;
    }
    return new CustomerProfileResponse(
        c.getCustomerId(),
        c.getFirstName(),
        c.getLastName(),
        c.getEmail(),
        c.getPhoneNumber(),
        c.getRole(),
        c.getEnabled(),
        c.getEmailVerified(),
        c.getEmailVerifiedAt(),
        c.getMfaEnabled(),
        c.getOauthProvider(),
        c.getLoyaltyPoints(),
        c.getLoyaltyTier(),
        c.getStoreCredit(),
        c.getCustomerGroupId(),
        c.getTaxId(),
        c.getCreatedAt(),
        c.getLastLoginAt());
  }
}
