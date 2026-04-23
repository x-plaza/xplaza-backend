/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.catalog.domain.entity;

import jakarta.persistence.*;

import lombok.*;

/**
 * Per-locale translation of a product. Storefront responses substitute these
 * values into the canonical {@link Product} payload at render time.
 */
@Entity
@Table(name = "product_translations", uniqueConstraints = @UniqueConstraint(columnNames = { "product_id", "locale" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTranslation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(nullable = false, length = 10)
  private String locale;

  @Column(length = 255)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "seo_title", length = 255)
  private String seoTitle;

  @Column(name = "seo_description", length = 500)
  private String seoDescription;
}
