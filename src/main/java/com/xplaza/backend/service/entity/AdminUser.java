/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service.entity;

import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUser {
  private Long id;
  private String fullName;
  private String email;
  private Long roleId;
  private List<Long> shopIds;
  // add other fields as needed
}
