/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.common.util;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ApiResponse {
  public long responseTime;
  public String responseType;
  public int status;
  public String response;
  public String message;
  public String data;

  public ApiResponse(long responseTime, String responseType, int status, String response, String message, String data) {
    this.responseTime = responseTime;
    this.responseType = responseType;
    this.status = status;
    this.response = response;
    this.message = message;
    this.data = data;
  }

  public String getTimestamp() {
    return LocalDateTime.now().toString();
  }
}
