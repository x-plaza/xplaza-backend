/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "delivery_costs")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCostDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long deliveryCostId;

  Double delivery_slab_start_range;

  Double delivery_slab_end_range;

  Double deliveryCost;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_currency_id")
  CurrencyDao currency;
}