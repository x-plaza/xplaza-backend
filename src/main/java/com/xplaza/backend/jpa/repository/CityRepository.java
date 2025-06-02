/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.City;

public interface CityRepository extends JpaRepository<City, Long> {
  @Query(value = "select city_name from cities where city_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from cities where city_id = ?1", nativeQuery = true)
  City findCityById(Long id);
}
