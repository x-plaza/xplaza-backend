/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long couponId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_currency_id")
  private Currency currency;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_discount_type_id")
  private DiscountTypeDao discountType;

  private String couponCode;
  private Double discountValue;
  private Date validFrom;
  private Date validTo;
}