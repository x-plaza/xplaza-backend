/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
  @Query(value = "select product_name from products where product_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from products where product_id = ?1", nativeQuery = true)
  Product findProductById(Long id);

  @Modifying
  @Transactional
  @Query(value = "update products set quantity = ?2 where product_id = ?1", nativeQuery = true)
  void updateProductInventory(Long id, Long new_quantity);
}
