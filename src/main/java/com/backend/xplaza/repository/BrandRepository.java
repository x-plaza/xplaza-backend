/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.xplaza.model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
  @Query(value = "select brand_name from brands where brand_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from brands where brand_id = ?1", nativeQuery = true)
  Brand findBrandById(Long id);

  @Query(value = "select coalesce ((select true from brands b where b.brand_name = ?1), false)", nativeQuery = true)
  boolean existsByName(String name);
}
