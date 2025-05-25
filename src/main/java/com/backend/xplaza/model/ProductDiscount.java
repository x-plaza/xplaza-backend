/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.model;

import java.util.Date;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_discounts")
public class ProductDiscount {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_discount_id")
  private Long id;

  @Column(name = "fk_product_id")
  private Long product_id;

  @Column(name = "fk_discount_type_id")
  private Long discount_type_id;

  @Column(name = "discount_amount")
  private Double discount_amount;

  @Column(name = "fk_currency_id")
  private Long currency_id;

  @Column(name = "discount_start_date")
  private Date start_date;

  @Column(name = "discount_end_date")
  private Date end_date;
}
