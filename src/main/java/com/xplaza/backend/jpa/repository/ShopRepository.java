/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {
  @Query(value = "select shop_name from shops where shop_id = ?1", nativeQuery = true)
  String getName(Long id);
}
