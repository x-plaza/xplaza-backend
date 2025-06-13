/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryCost {
  private Long deliveryCostId;
  private String deliveryCostName;
  Double deliveryCost;
  Double delivery_slab_start_range;

  Double delivery_slab_end_range;
}
