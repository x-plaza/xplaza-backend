/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long brandId;

  private String brandName;
  private String brandDescription;

  @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
  private List<ProductDao> products = new ArrayList<>();
}