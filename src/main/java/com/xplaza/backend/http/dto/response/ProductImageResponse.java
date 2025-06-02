/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.dto.response;

import lombok.Data;

@Data
public class ProductImageResponse {
  private Long id;
  private String name;
  private String path;
  private Long productId;
  // add other fields as needed
}
