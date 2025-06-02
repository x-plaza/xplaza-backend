/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
  private Long id;
  private Long productId;
  private Long orderId;
  private Long quantity;
  private Double productSellingPrice;
  private Double productBuyingPrice;
  private String itemName;
  private String itemVarTypeName;
  private Long itemVarTypeValue;
  private String itemCategory;
  private String quantityType;
  private Double unitPrice;
  private Double itemTotalPrice;
  private String itemImage;
  private Long currencyId;
  // add other fields as needed
}
