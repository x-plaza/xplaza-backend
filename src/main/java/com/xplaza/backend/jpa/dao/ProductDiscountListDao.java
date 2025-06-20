/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

// This is a view-like DTO, not a JPA entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDiscountListDao {
  @Id
  @Column(name = "product_discount_id")
  private Long id;

  @Column(name = "fk_product_id")
  private Long productId;

  @Column(name = "product_name")
  private String productName;

  @Column(name = "fk_discount_type_id")
  private Long discountTypeId;

  @Column(name = "discount_type_name")
  private String discountTypeName;

  @Column(name = "discount_amount")
  private Double discountAmount;

  @Column(name = "fk_currency_id")
  private Long currencyId;

  @Column(name = "currency_name")
  private String currencyName;

  @Column(name = "currency_sign")
  private String currencySign;

  @Column(name = "valid_from")
  private String discountStartDate;

  @Column(name = "valid_to")
  private String discountEndDate;
}
