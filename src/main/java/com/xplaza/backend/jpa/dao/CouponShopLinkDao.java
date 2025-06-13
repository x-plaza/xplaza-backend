/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "coupon_shop_link")
@IdClass(CouponShopLinkIdDao.class)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponShopLinkDao {
  @Id
  Long shopId;

  @Id
  Long couponId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shop_id")
  ShopDao shop;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coupon_id")
  CouponDao coupon;
}