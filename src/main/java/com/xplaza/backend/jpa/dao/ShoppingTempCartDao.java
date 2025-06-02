/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "shopping_temp_carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingTempCartDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long shoppingTempCartId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_product_id")
  private ProductDao product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_customer_id")
  private CustomerDao customer;

  private Integer quantity;
}