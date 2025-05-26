/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.model;

import java.util.List;

import jakarta.persistence.*;

import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "day_names")
public class DeliveryScheduleList {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "day_id")
  private Long day_id;

  @Column(name = "day_name")
  private String day_name;

  @OneToMany(mappedBy = "deliveryScheduleList")
  @OrderBy("delivery_schedule_start ASC")
  private List<DeliverySchedule> delivery_schedules;
}
