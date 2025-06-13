/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.LocationDao;

public interface LocationRepository extends JpaRepository<LocationDao, Long> {
  @Query(value = "select location_name from locations where location_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from locations where location_id = ?1", nativeQuery = true)
  LocationDao findLocationById(Long id);

  @Query(value = "SELECT * FROM locations WHERE city_id = ?1", nativeQuery = true)
  List<LocationDao> findByCityId(Long cityId);

  @Query(value = "SELECT * FROM locations WHERE state_id = ?1", nativeQuery = true)
  List<LocationDao> findByStateId(Long stateId);

  @Query(value = "SELECT * FROM locations WHERE country_id = ?1", nativeQuery = true)
  List<LocationDao> findByCountryId(Long countryId);
}
