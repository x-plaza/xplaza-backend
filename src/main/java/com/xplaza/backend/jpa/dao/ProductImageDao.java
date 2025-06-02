/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "product_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productImagesId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_product_id")
  private ProductDao product;

  private String productImageName;
  private String productImagePath;
  private Integer createdBy;
  private Date createdAt;
  private Integer lastUpdatedBy;
  private Date lastUpdatedAt;
}