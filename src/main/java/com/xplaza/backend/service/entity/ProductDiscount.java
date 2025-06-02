/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service.entity;

import java.util.Date;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDiscount {
  private Long id;
  private Long productId;
  private Long discountTypeId;
  private Double discountAmount;
  private Long currencyId;
  private Date startDate;
  private Date endDate;
}
