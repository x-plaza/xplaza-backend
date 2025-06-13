/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "product_images")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long productImagesId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_product_id")
  ProductDao product;

  String productImageName;

  String productImagePath;

  Integer createdBy;

  Date createdAt;

  Integer lastUpdatedBy;

  Date lastUpdatedAt;
}