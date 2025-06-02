/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {
  private Long orderId;
  private String invoiceNumber;
  private Double totalPrice;
  private Double discountAmount;
  private Double netTotal;
  private Double grandTotalPrice;
  private String deliveryAddress;
  private String customerId;
  private String customerName;
  private String mobileNo;
  // add other fields as needed
}
