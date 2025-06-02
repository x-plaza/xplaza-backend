/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.dto.request;

import lombok.Data;

@Data
public class ProductRequest {
  private String name;
  private String description;
  private Long brandId;
  private Long categoryId;
  private Long productVarTypeId;
  private Double productVarTypeValue;
  private Double buyingPrice;
  private Double sellingPrice;
  // add other fields as needed
}
