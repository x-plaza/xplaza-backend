/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.xplaza.model.DeliveryScheduleList;

public interface DeliveryScheduleListRepository extends JpaRepository<DeliveryScheduleList, Long> {
  @Query(value = "select dn.* from day_names dn", nativeQuery = true)
  List<DeliveryScheduleList> findAllItem();
}
