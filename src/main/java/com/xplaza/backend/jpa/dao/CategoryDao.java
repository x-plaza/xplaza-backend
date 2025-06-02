/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long categoryId;

  private String categoryName;
  private String categoryDescription;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_category")
  private CategoryDao parentCategory;

  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private List<ProductDao> products = new ArrayList<>();
}