/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.Immutable;

@Table(name = "top_product")
@Immutable
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class TopProductDao {
  @Id
  Long id;

  Long productId;

  String productName;

  Long monthlySoldUnit;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "shop_id")
  ShopDao shop;
}