/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.jpa.dao.CouponDao;

public interface CouponRepository extends JpaRepository<CouponDao, Long> {
  @Query(value = "select coupon_code from coupons where coupon_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Modifying
  @Transactional
  @Query(value = "update coupons set coupon_amount=?1, fk_currency_id=?2, coupon_start_date=?3, coupon_end_date=?4, max_coupon_amount=?5,"
      +
      "fk_discount_type_id=?6, is_active=?7, min_shopping_amount=?8 where coupon_id=?9", nativeQuery = true)
  void update(Double coupon_amount, Long currency_id, Date coupon_start_date, Date coupon_end_date,
      Double max_coupon_amount,
      Long fk_discount_type_id, Boolean is_active, Double min_shopping_amount, Long coupon_id);

  @Query(value = "select coalesce ((select true from coupons c where c.coupon_code = ?1), false)", nativeQuery = true)
  boolean existsByName(String coupon_code);
}