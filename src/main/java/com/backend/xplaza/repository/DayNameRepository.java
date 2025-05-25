/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.xplaza.model.Day;

public interface DayNameRepository extends JpaRepository<Day, Long> {
  @Query(value = "select day_name from day_names where day_id = ?1", nativeQuery = true)
  String getName(Long id);
}
