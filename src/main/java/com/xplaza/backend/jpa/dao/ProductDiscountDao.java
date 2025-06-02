/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "product_discounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDiscountDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productDiscountId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_discount_type_id")
  private DiscountTypeDao discountType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_product_id")
  private ProductDao product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_currency_id")
  private CurrencyDao currency;

  private Double discountValue;
  private Date validFrom;
  private Date validTo;
}