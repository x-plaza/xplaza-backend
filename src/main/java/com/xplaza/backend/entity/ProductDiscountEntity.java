/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.entity;

import java.util.Date;

import jakarta.persistence.*;

import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_discounts")
public class ProductDiscountEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_discount_id")
  private Long id;

  @Column(name = "fk_product_id")
  private Long productId;

  @Column(name = "fk_discount_type_id")
  private Long discountTypeId;

  @Column(name = "discount_amount")
  private Double discountAmount;

  @Column(name = "fk_currency_id")
  private Long currencyId;

  @Column(name = "discount_start_date")
  private Date startDate;

  @Column(name = "discount_end_date")
  private Date endDate;
}
