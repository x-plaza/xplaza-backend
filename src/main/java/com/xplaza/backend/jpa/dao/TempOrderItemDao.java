/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "temp_order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TempOrderItemDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long tempOrderItemId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_temp_order_id")
  private TempOrderDao tempOrder;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_product_id")
  private ProductDao product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_currency_id")
  private CurrencyDao currency;

  private Integer quantity;
  private Double price;
}