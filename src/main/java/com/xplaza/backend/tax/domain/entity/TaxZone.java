/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.tax.domain.entity;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "tax_zones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxZone {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tax_zone_id")
  private Long taxZoneId;

  @Column(name = "name", nullable = false)
  private String name;
  @Column(name = "country_code", length = 2, nullable = false)
  private String countryCode;
  @Column(name = "region")
  private String region;
  @Column(name = "postal_code_pattern")
  private String postalCodePattern;
}
