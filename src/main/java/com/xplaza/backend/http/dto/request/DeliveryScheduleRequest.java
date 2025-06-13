/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.dto.request;

import java.sql.Time;

import lombok.Data;

@Data
public class DeliveryScheduleRequest {
  private Time deliveryScheduleStart;
  private Time deliveryScheduleEnd;
  private Long dayId;
  // add other fields as needed
}
