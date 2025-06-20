/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "shopping_temp_carts")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingTempCartDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long shoppingTempCartId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_product_id")
  ProductDao product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_customer_id")
  CustomerDao customer;

  Integer itemQuantity;
}