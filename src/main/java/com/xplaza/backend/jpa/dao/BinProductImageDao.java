/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "bin_product_images")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BinProductImageDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long binProductImageId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_bin_product_id")
  private BinProductDao binProduct;

  private String imageData; // Adjust type as needed
}