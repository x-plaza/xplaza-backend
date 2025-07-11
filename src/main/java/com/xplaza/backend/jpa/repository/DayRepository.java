/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.DayDao;

public interface DayRepository extends JpaRepository<DayDao, Long> {
  @Query(value = "select day_name from day_names where day_id = ?1", nativeQuery = true)
  String getName(Long id);
}
