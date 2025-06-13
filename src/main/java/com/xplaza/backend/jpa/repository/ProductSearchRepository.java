/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.service.entity.ProductSearch;

public interface ProductSearchRepository extends JpaRepository<ProductSearch, Long> {
  @Query(value = "select p.product_id, p.product_name from products p \n" +
      "where p.fk_shop_id = ?1 and LOWER(p.product_name) LIKE LOWER(CONCAT('%', ?2, '%')) \n" +
      "ORDER BY p.product_name", nativeQuery = true)
  List<ProductSearch> findProductListByName(Long shop_id, String product_name);
}
