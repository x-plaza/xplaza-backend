/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.dao;

import java.util.*;

import jakarta.persistence.*;

import lombok.*;

@Table(name = "day_names")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DayDao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long dayId;

  String dayName;

  @OneToMany(mappedBy = "dayName", fetch = FetchType.LAZY)
  List<DeliveryScheduleDao> deliverySchedules;
}