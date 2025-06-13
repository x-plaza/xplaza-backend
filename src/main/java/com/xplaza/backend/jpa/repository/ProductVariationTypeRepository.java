/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.jpa.dao.ProductVariationTypeDao;

@Repository
public interface ProductVariationTypeRepository extends JpaRepository<ProductVariationTypeDao, Long> {
  @Query(value = "select var_type_name from product_variation_types where product_var_type_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from product_variation_types where product_var_type_id = ?1", nativeQuery = true)
  ProductVariationTypeDao findProdVarTypeById(Long id);

  @Query(value = "select * from product_variation_types where var_type_name = ?1", nativeQuery = true)
  ProductVariationTypeDao findByVarTypeName(String varTypeName);
}
