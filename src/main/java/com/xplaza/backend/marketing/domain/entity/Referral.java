/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.marketing.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;

/**
 * Referral record. A customer (the {@code referrer}) sends an invitation to an
 * email address; once that referee registers and places their first order the
 * referral is marked {@code REWARDED} and the referrer is credited.
 */
@Entity
@Table(name = "referrals", indexes = {
    @Index(name = "idx_referrals_referrer", columnList = "referrer_id"),
    @Index(name = "idx_referrals_email", columnList = "referee_email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Referral {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "referrer_id", nullable = false)
  private Long referrerId;

  @Column(name = "referee_email", nullable = false, length = 255)
  private String refereeEmail;

  @Column(name = "referee_id")
  private Long refereeId;

  @Column(name = "code", nullable = false, unique = true, length = 40)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  @Builder.Default
  private ReferralStatus status = ReferralStatus.PENDING;

  @Column(name = "rewarded_at")
  private Instant rewardedAt;

  @Column(name = "reward_amount", precision = 14, scale = 2)
  private BigDecimal rewardAmount;

  @Column(name = "created_at", nullable = false)
  @Builder.Default
  private Instant createdAt = Instant.now();

  public enum ReferralStatus {
    PENDING,
    ACCEPTED,
    REWARDED,
    EXPIRED,
    CANCELLED
  }
}
