/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.dto.response;

import lombok.Data;

@Data
public class ShopResponse {
  private Long id;
  private String name;
  private String owner;
  private String address;
  private String description;
  private Long locationId;
}
