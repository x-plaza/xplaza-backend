/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.model;

import java.sql.Time;
import java.text.SimpleDateFormat;

import jakarta.persistence.*;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.xplaza.backend.common.util.SqlTimeDeserializer;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_schedules")
public class DeliveryScheduleDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "delivery_schedule_id")
  private Long delivery_schedule_id;

  @JsonFormat(pattern = "HH:mm")
  @JsonDeserialize(using = SqlTimeDeserializer.class)
  @Column(name = "delivery_schedule_start")
  private Time delivery_schedule_start;

  public String getDelivery_schedule_start() {
    if (delivery_schedule_start != null)
      return new SimpleDateFormat("HH:mm").format(delivery_schedule_start);
    return null;
  }

  @JsonFormat(pattern = "HH:mm")
  @JsonDeserialize(using = SqlTimeDeserializer.class)
  @Column(name = "delivery_schedule_end")
  private Time delivery_schedule_end;

  public String getDelivery_schedule_end() {
    if (delivery_schedule_end != null)
      return new SimpleDateFormat("HH:mm").format(delivery_schedule_end);
    return null;
  }

  @Column(name = "fk_day_id")
  private Long day_id;

  @Column(name = "day_name")
  private String day_name;
}
