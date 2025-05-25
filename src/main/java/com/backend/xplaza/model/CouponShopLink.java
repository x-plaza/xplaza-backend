/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.model;

import jakarta.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon_shop_link")
@IdClass(CouponShopLinkId.class)
public class CouponShopLink {
  @Id
  @Column(name = "coupon_id")
  private Long coupon_id;

  @Id
  @Column(name = "shop_id")
  private Long shop_id;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "coupon_id", insertable = false, updatable = false)
  private Coupon coupon;
}
