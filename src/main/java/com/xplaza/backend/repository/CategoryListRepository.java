/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.model.CategoryList;

public interface CategoryListRepository extends JpaRepository<CategoryList, Long> {
  @Query(value = "select c1.*, c2.category_name  as parent_category_name from categories c1 \n" +
      "left join categories c2 on c2.category_id = c1.parent_category", nativeQuery = true)
  List<CategoryList> findAllCategories();

  @Query(value = "select c1.*, c2.category_name  as parent_category_name from categories c1 " +
      " left join categories c2 on c2.category_id = c1.parent_category where c1.category_id = ?1", nativeQuery = true)
  CategoryList findCategoryById(Long id);
}
