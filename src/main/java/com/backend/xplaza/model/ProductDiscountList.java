/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_discounts")
public class ProductDiscountList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_discount_id")
  private Long id;

  @Column(name = "fk_product_id")
  private Long product_id;

  @Column(name = "product_name")
  private String product_name;

  @Column(name = "fk_discount_type_id")
  private Long discount_type_id;

  @Column(name = "discount_type_name")
  private String discount_type_name;

  @Column(name = "discount_amount")
  private Double discount_amount;

  @Column(name = "fk_currency_id")
  private Long currency_id;

  @Column(name = "currency_name")
  private String currency_name;

  @Column(name = "currency_sign")
  private String currency_sign;

  @Column(name = "discount_start_date")
  private Date start_date;

  public String getStart_date() {
    if (start_date != null)
      return new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(start_date);
    return null;
  }

  @Column(name = "discount_end_date")
  private Date end_date;

  public String getEnd_date() {
    if (end_date != null)
      return new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(end_date);
    return null;
  }
}
