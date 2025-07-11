/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDiscountList {
  private Long id;
  private Long productId;
  private String productName;
  private Long discountTypeId;
  private String discountTypeName;
  private Double discountAmount;
  private Long currencyId;
  private String currencyName;
  private String currencySign;
  private String discountStartDate;
  private String discountEndDate;
}