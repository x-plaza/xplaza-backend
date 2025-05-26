/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.model;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_costs")
public class DeliveryCost {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "delivery_cost_id")
  private Long id;

  @Column(name = "delivery_slab_start_range")
  private Double start_range;

  @Column(name = "delivery_slab_end_range")
  private Double end_range;

  @Column(name = "delivery_cost")
  private Double cost;

  @Column(name = "fk_currency_id")
  private Long currency_id;
}
