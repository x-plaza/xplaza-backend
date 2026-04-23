/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.tax.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "tax_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxRule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tax_rule_id")
  private Long taxRuleId;

  @Column(name = "tax_zone_id", nullable = false)
  private Long taxZoneId;
  @Column(name = "name", nullable = false)
  private String name;
  @Column(name = "rate", precision = 6, scale = 4, nullable = false)
  private BigDecimal rate;
  @Column(name = "category")
  private String category;
  @Column(name = "priority")
  @Builder.Default
  private Integer priority = 0;
  @Column(name = "compound")
  @Builder.Default
  private Boolean compound = false;
  @Column(name = "active")
  @Builder.Default
  private Boolean active = true;
}
