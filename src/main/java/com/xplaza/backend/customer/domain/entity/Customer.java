/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.customer.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Table(name = "customers")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long customerId;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  private String phoneNumber;

  @Column(nullable = false)
  @Builder.Default
  private String role = "CUSTOMER";

  @Column(nullable = false)
  @Builder.Default
  private Boolean enabled = true;

  // ---------- Email verification ----------
  @Column(name = "verified_email", nullable = false)
  @Builder.Default
  private Boolean emailVerified = false;

  @Column(name = "verified_email_at")
  private Instant emailVerifiedAt;

  // ---------- Account lockout ----------
  @Column(name = "failed_login_attempts", nullable = false)
  @Builder.Default
  private Integer failedLoginAttempts = 0;

  @Column(name = "locked_until")
  private Instant lockedUntil;

  // ---------- MFA ----------
  @Column(name = "mfa_enabled", nullable = false)
  @Builder.Default
  private Boolean mfaEnabled = false;

  @Column(name = "mfa_secret", length = 200)
  private String mfaSecret;

  // ---------- OAuth ----------
  @Column(name = "oauth_provider", length = 30)
  private String oauthProvider;

  @Column(name = "oauth_subject", length = 200)
  private String oauthSubject;

  // ---------- Loyalty ----------
  @Column(name = "loyalty_points", nullable = false)
  @Builder.Default
  private Long loyaltyPoints = 0L;

  @Column(name = "loyalty_tier", length = 20)
  @Builder.Default
  private String loyaltyTier = "BRONZE";

  @Column(name = "store_credit", precision = 14, scale = 2)
  @Builder.Default
  private BigDecimal storeCredit = BigDecimal.ZERO;

  // ---------- B2B ----------
  @Column(name = "customer_group_id")
  private Long customerGroupId;

  @Column(name = "tax_id", length = 50)
  private String taxId;

  // ---------- Lifecycle ----------
  private LocalDateTime createdAt;
  private LocalDateTime lastLoginAt;

  @PrePersist
  protected void onCreate() {
    if (createdAt == null) createdAt = LocalDateTime.now();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role));
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return lockedUntil == null || lockedUntil.isBefore(Instant.now());
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return Boolean.TRUE.equals(enabled);
  }
}
