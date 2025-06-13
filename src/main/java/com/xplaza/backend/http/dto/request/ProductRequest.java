/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
  private Long productId;
  private String productName;
  private String productDescription;
  private Double productPrice;
  private Integer quantity;
  private Boolean isTrending;
  private Long shopId;
  private Long categoryId;
  private Long brandId;
}
