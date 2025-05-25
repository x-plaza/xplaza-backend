/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.xplaza.model.State;

public interface StateRepository extends JpaRepository<State, Long> {
  @Query(value = "select state_name from states where state_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from states where state_id = ?1", nativeQuery = true)
  State findStateById(Long id);
}
