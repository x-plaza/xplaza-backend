/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "coupon_shop_link")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CouponShopLinkIdDao.class)
public class CouponShopLinkDao {
  @Id
  @Column(name = "shop_id")
  Long shopId;

  @Id
  @Column(name = "coupon_id")
  Long couponId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shop_id", insertable = false, updatable = false)
  ShopDao shop;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "coupon_id", insertable = false, updatable = false)
  CouponDao coupon;
}