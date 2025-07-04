/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.ShopDao;

public interface ShopRepository extends JpaRepository<ShopDao, Long> {
  @Query(value = "select shop_name from shops where shop_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "SELECT * FROM shops WHERE location_id = ?1", nativeQuery = true)
  List<ShopDao> findByLocationId(Long locationId);

  @Query(value = "SELECT * FROM shops WHERE shop_owner = ?1", nativeQuery = true)
  List<ShopDao> findByShopOwner(Long ownerId);
}
