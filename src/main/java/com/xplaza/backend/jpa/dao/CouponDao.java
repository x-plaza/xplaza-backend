/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "coupons")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long couponId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_currency_id")
  CurrencyDao currency;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_discount_type_id")
  DiscountTypeDao discountType;

  String couponCode;

  Double discountValue;

  Date validFrom;

  Date validTo;
}