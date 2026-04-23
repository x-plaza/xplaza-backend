/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.shop.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

/**
 * Marketplace shop. v1.1.0 adds {@code commissionRate} (per-shop override of
 * the marketplace default), {@code payoutAccount} (Stripe account or bank
 * reference) and soft-delete bookkeeping via {@code deleted_at}.
 */
@Table(name = "shops")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE shops SET deleted_at = CURRENT_TIMESTAMP WHERE shop_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Shop {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long shopId;

  private String shopName;

  private String shopDescription;

  private String shopAddress;

  @Column(name = "fk_location_id")
  private Long locationId;

  private String shopOwner;

  @Column(name = "commission_rate", precision = 5, scale = 4)
  private BigDecimal commissionRate;

  @Column(name = "payout_account", length = 255)
  private String payoutAccount;

  @Column(name = "payout_currency", length = 3)
  private String payoutCurrency;

  @Column(name = "deleted_at")
  private Instant deletedAt;
}
