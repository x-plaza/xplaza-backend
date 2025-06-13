/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "bin_order_items")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BinOrderItemDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long binOrderItemId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_bin_order_id")
  private BinOrderDao binOrder;

  private String itemData; // Adjust type as needed
}