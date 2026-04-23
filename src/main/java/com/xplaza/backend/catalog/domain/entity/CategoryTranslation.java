/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.catalog.domain.entity;

import jakarta.persistence.*;

import lombok.*;

/**
 * Per-locale translation of a category.
 */
@Entity
@Table(name = "category_translations", uniqueConstraints = @UniqueConstraint(columnNames = { "category_id", "locale" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryTranslation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "category_id", nullable = false)
  private Long categoryId;

  @Column(nullable = false, length = 10)
  private String locale;

  @Column(length = 255)
  private String name;

  @Column(length = 500)
  private String description;
}
