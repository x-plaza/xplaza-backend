/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.model.ProductVariationType;

public interface ProductVariationTypeRepository extends JpaRepository<ProductVariationType, Long> {
  @Query(value = "select var_type_name from product_variation_types where product_var_type_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from product_variation_types where product_var_type_id = ?1", nativeQuery = true)
  ProductVariationType findProdVarTypeById(Long id);
}
