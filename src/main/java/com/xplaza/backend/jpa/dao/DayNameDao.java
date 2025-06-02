/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "day_names")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayNameDao {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dayId;

  private String dayName;

  @OneToMany(mappedBy = "dayName", fetch = FetchType.LAZY)
  private List<DeliveryScheduleDao> deliverySchedules = new ArrayList<>();
}