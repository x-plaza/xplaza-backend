/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "temp_order_items")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TempOrderItemDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long tempOrderItemId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_temp_order_id")
  TempOrderDao tempOrder;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_product_id")
  ProductDao product;

  String orderItemName;

  Double orderItemPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_currency_id")
  CurrencyDao currency;

  Integer quantity;
}