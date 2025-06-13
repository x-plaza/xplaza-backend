/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class AdminUserRequest {
  private String fullName;
  private String email;
  private Long roleId;
  private List<Long> shopIds;
  private String userName;
  private String password;
  private String salt;
  private String confirmationCode;
}
