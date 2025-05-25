/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.xplaza.model.CouponList;

public interface CouponListRepository extends JpaRepository<CouponList, Long> {
  @Query(value = "select cou.*, c.currency_name, c.currency_sign, dt.discount_type_name " +
      "from coupons cou " +
      "left join currencies c on cou.fk_currency_id = c.currency_id " +
      "left join discount_types dt on dt.discount_type_id = cou.fk_discount_type_id", nativeQuery = true)
  List<CouponList> findAllCoupons();

  @Query(value = "select cou.*, c.currency_name, c.currency_sign, dt.discount_type_name " +
      "from coupons cou " +
      "left join currencies c on cou.fk_currency_id = c.currency_id " +
      "left join discount_types dt on dt.discount_type_id = cou.fk_discount_type_id " +
      "left join coupon_shop_link csl on cou.coupon_id = csl.coupon_id " +
      "left join admin_user_shop_link ausl on ausl.shop_id = csl.shop_id " +
      "where ausl.admin_user_id = ?1 or ausl.admin_user_id is null", nativeQuery = true)
  List<CouponList> findCouponsByUserID(Long user_id);
}
