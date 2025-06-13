/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.DeliveryScheduleDao;

public interface DeliveryScheduleRepository extends JpaRepository<DeliveryScheduleDao, Long> {
  @Query(value = "select * from delivery_schedules where delivery_schedule_id = ?1", nativeQuery = true)
  DeliveryScheduleDao findDeliveryScheduleById(Long id);
}
