/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.model.DeliveryScheduleDetails;

public interface DeliveryScheduleDetailsRepository extends JpaRepository<DeliveryScheduleDetails, Long> {
  @Query(value = "select ds.*,dn.day_name from delivery_schedules ds \n" +
      "left join day_names dn on ds.fk_day_id = dn.day_id \n" +
      "where ds.delivery_schedule_id = ?1", nativeQuery = true)
  DeliveryScheduleDetails findDeliveryScheduleDetailsById(Long id);
}
