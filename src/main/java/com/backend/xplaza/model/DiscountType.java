/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "discount_types")
public class DiscountType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "discount_type_id")
  private Long id;

  @Column(name = "discount_type_name")
  private String name;
}
