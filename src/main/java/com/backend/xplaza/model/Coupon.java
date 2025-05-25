/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupons")
public class Coupon {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "coupon_id")
  private Long id;

  @Column(name = "coupon_code")
  private String coupon_code;

  @Column(name = "coupon_amount")
  private Double amount;

  @Column(name = "max_coupon_amount")
  private Double max_amount;

  @Column(name = "fk_currency_id")
  private Long currency_id;

  @Column(name = "fk_discount_type_id")
  private Long discount_type_id;

  @Column(name = "coupon_start_date")
  private Date start_date;

  @Column(name = "coupon_end_date")
  private Date end_date;

  @Column(name = "is_active")
  private Boolean is_active;

  @Column(name = "min_shopping_amount")
  private Double min_shopping_amount;

  @OneToMany(mappedBy = "coupon")
  private List<CouponShopLink> couponShopLinks;
}
