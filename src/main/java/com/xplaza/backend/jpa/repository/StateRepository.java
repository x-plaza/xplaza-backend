/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.StateDao;

public interface StateRepository extends JpaRepository<StateDao, Long> {
  @Query(value = "select * from states where state_id = ?1", nativeQuery = true)
  StateDao findStateById(Long id);

  @Query(value = "SELECT * FROM states WHERE country_id = ?1", nativeQuery = true)
  List<StateDao> findByCountryId(Long countryId);
}
