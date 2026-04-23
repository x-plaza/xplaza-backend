/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.b2b.domain.entity;

import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;

/**
 * Price list. Targets a specific {@link CustomerGroup} (B2B contract pricing)
 * or, when {@code customerGroupId} is null, can be applied to any customer
 * (e.g. a public flash-sale price list).
 */
@Entity
@Table(name = "price_lists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceList {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code", nullable = false, unique = true, length = 50)
  private String code;

  @Column(name = "name", nullable = false, length = 150)
  private String name;

  @Column(name = "customer_group_id")
  private Long customerGroupId;

  @Column(name = "currency", nullable = false, length = 3)
  @Builder.Default
  private String currency = "USD";

  @Column(name = "starts_at")
  private Instant startsAt;

  @Column(name = "ends_at")
  private Instant endsAt;

  @Column(name = "is_active")
  @Builder.Default
  private Boolean active = true;

  public boolean isApplicableNow() {
    if (Boolean.FALSE.equals(active)) {
      return false;
    }
    var now = Instant.now();
    if (startsAt != null && now.isBefore(startsAt)) {
      return false;
    }
    if (endsAt != null && now.isAfter(endsAt)) {
      return false;
    }
    return true;
  }
}
