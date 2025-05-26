/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.repository;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.model.ShopList;

public interface ShopListRepository extends JpaRepository<ShopList, Long> {
  @Query(value = "select s.*, l.location_name from shops s " +
      "left join locations l on s.fk_location_id = l.location_id", nativeQuery = true)
  List<ShopList> findAllShopList();

  @Query(value = "select s.*, l.location_name from shops s " +
      "left join locations l on s.fk_location_id = l.location_id where s.shop_id = ?1", nativeQuery = true)
  ShopList findShopListById(Long id);

  @Query(value = "select s.*, l.location_name from shops s " +
      "left join locations l on s.fk_location_id = l.location_id " +
      "left join admin_user_shop_link ausl on ausl.shop_id = s.shop_id " +
      "where ausl.admin_user_id = ?1", nativeQuery = true)
  List<ShopList> findAllShopListByUserID(@Valid Long user_id);

  @Query(value = "select s.*, l.location_name from shops s " +
      "left join locations l on s.fk_location_id = l.location_id where s.fk_location_id = ?1", nativeQuery = true)
  List<ShopList> findShopListByLocationId(Long id);
}
