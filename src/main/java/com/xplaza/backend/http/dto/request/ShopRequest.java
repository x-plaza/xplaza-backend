/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.dto.request;

import lombok.Data;

@Data
public class ShopRequest {
  private String name;
  private String owner;
  private String address;
  private String description;
  private Long locationId;
}
