/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.dto.request;

import lombok.Data;

@Data
public class DeliveryCostRequest {
  private String name;
  private Double cost;
  // add other fields as needed
}
