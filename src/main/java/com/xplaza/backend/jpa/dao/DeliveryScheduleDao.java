/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "delivery_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryScheduleDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long deliveryScheduleId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_day_id")
  private DayNameDao dayName;

  private String deliveryTime;
}