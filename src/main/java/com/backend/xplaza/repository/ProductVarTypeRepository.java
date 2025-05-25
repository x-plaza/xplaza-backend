/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.xplaza.model.ProductVarType;

public interface ProductVarTypeRepository extends JpaRepository<ProductVarType, Long> {
  @Query(value = "select var_type_name from product_variation_types where product_var_type_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from product_variation_types where product_var_type_id = ?1", nativeQuery = true)
  ProductVarType findProdVarTypeById(Long id);
}
