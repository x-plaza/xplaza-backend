/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupons")
public class CouponList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "coupon_id")
  private Long id;

  @Column(name = "coupon_code")
  private String coupon_code;

  @Column(name = "is_active")
  private Boolean is_active;

  @Column(name = "coupon_amount")
  private Double amount;

  @Column(name = "max_coupon_amount")
  private Double max_amount;

  @Column(name = "fk_currency_id")
  private Long currency_id;

  @Column(name = "currency_name")
  private String currency_name;

  @Column(name = "currency_sign")
  private String currency_sign;

  @Column(name = "fk_discount_type_id")
  private Long discount_type_id;

  @Column(name = "discount_type_name")
  private String discount_type_name;

  @Column(name = "coupon_start_date")
  private Date start_date;

  public String getStart_date() {
    if (start_date != null)
      return new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(start_date);
    return null;
  }

  @Column(name = "coupon_end_date")
  private Date end_date;

  public String getEnd_date() {
    if (end_date != null)
      return new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(end_date);
    return null;
  }

  @Column(name = "min_shopping_amount")
  private Double min_shopping_amount;
}
