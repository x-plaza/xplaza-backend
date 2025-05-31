/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.dao;

import lombok.Data;

@Data
public class ProductDiscountDAO {
  private Long id;
  private Long productId;
  private Long discountTypeId;
  private Double discountAmount;
  private Long currencyId;
  private java.util.Date startDate;
  private java.util.Date endDate;
}
