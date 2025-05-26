/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.model.DeliverySchedule;

public interface DeliveryScheduleRepository extends JpaRepository<DeliverySchedule, Long> {
  @Query(value = "select * from delivery_schedules where delivery_schedule_id = ?1", nativeQuery = true)
  DeliverySchedule findDeliveryScheduleById(Long id);
}
