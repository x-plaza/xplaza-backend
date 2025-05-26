/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
  @Query(value = "select location_name from locations where location_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from locations where location_id = ?1", nativeQuery = true)
  Location findLocationById(Long id);
}
