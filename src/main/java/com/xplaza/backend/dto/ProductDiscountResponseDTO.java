/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ProductDiscountResponseDTO {
  private Long id;
  private Long productId;
  private String productName;
  private Long discountTypeId;
  private String discountTypeName;
  private Double discountAmount;
  private Long currencyId;
  private String currencyName;
  private String currencySign;
  private Date startDate;
  private Date endDate;
}
