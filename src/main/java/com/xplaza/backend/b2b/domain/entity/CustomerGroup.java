/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.b2b.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;

/**
 * B2B customer group. Customers in a group inherit the group's negotiated
 * discount and tax-exemption flag, and become eligible for any price list tied
 * to the group.
 */
@Entity
@Table(name = "customer_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code", nullable = false, unique = true, length = 50)
  private String code;

  @Column(name = "name", nullable = false, length = 150)
  private String name;

  @Column(name = "description", length = 500)
  private String description;

  @Column(name = "discount_percent", precision = 5, scale = 2)
  @Builder.Default
  private BigDecimal discountPercent = BigDecimal.ZERO;

  @Column(name = "tax_exempt")
  @Builder.Default
  private Boolean taxExempt = false;

  @Column(name = "created_at", nullable = false)
  @Builder.Default
  private Instant createdAt = Instant.now();
}
