/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {
  private Long productId;
  private Long orderId;
  private Long quantity;
  // add other fields as needed
}
