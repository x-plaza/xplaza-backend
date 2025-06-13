/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "categories")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long categoryId;

  String categoryName;

  String categoryDescription;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_category")
  CategoryDao parentCategory;
}