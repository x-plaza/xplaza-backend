/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "bin_orders")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BinOrderDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long binOrderId;

  private String orderData; // Adjust type as needed
  private Date deletedAt;

  @OneToMany(mappedBy = "binOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<BinOrderItemDao> binOrderItems = new ArrayList<>();
}