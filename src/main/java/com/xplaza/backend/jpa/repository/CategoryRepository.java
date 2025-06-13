/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.CategoryDao;

public interface CategoryRepository extends JpaRepository<CategoryDao, Long> {
  @Query(value = "select category_name from categories where category_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select coalesce ((select true from categories c where c.category_name = ?1), false)", nativeQuery = true)
  boolean existsByName(String name);

  @Query(value = "select coalesce ((select true from categories c where c.parent_category = ?1 limit 1), false)", nativeQuery = true)
  boolean hasChildCategory(Long id);
}
