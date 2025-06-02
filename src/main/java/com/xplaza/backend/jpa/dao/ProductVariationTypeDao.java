/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "product_variation_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariationTypeDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productVarTypeId;

  private String varTypeName;
  private String varTypeDescription;

  @OneToMany(mappedBy = "productVariationType", fetch = FetchType.LAZY)
  private List<ProductDao> products = new ArrayList<>();
}