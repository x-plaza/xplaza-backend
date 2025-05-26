/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.model;

import jakarta.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "top_product")
public class TopProduct {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "product_id")
  private Long product_id;

  @Column(name = "product_name")
  private String name;

  @Column(name = "monthly_sold_unit")
  private Double monthly_sold_unit;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "shop_id", insertable = false, updatable = false)
  private Dashboard dashboard;
}
