/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "bin_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BinProductDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long binProductId;

  private String productData; // Adjust type as needed

  @OneToMany(mappedBy = "binProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<BinProductImageDao> binProductImages = new ArrayList<>();
}