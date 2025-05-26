/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.model.CouponShopLink;

public interface CouponShopLinkRepository extends JpaRepository<CouponShopLink, Long> {
  @Modifying
  @Transactional
  @Query(value = "insert into coupon_shop_link values(?1, ?2)", nativeQuery = true)
  void insert(Long shop_id, Long coupon_id);

  @Modifying
  @Transactional
  @Query(value = "delete from coupon_shop_link where coupon_id = ?1", nativeQuery = true)
  void deleteByCouponID(Long id);
}
