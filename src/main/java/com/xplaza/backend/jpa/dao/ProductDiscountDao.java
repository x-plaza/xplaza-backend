/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "product_discounts")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDiscountDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long productDiscountId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_product_id")
  ProductDao product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_discount_type_id")
  DiscountTypeDao discountType;

  Double discountAmount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_currency_id")
  CurrencyDao currency;

  Date discountStartDate;

  Date discountEndDate;
}