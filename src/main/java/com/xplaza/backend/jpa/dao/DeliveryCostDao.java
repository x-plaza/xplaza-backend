/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "delivery_costs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryCostDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long deliveryCostId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_currency_id")
  private Currency currency;

  private Double cost;
}