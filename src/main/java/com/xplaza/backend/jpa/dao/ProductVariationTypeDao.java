/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.Immutable;

@Table(name = "product_variation_types")
@Immutable
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariationTypeDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long productVarTypeId;

  String varTypeName;

  String varTypeDescription;
}