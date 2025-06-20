/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "delivery_schedules")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryScheduleDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long deliveryScheduleId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_day_id")
  DayDao dayName;

  String deliveryTime;
}