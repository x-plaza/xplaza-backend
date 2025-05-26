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
public class DeliveryCostList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "delivery_cost_id")
  private Long id;

  @Column(name = "delivery_slab_range")
  private String delivery_slab_range;

  @Column(name = "delivery_cost")
  private Double cost;

  @Column(name = "fk_currency_id")
  private Long currency_id;

  @Column(name = "currency_name")
  private String currency_name;

  @Column(name = "currency_sign")
  private String currency_sign;
}
