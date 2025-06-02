/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.model.LocationList;

public interface LocationListRepository extends JpaRepository<LocationList, Long> {
  @Query(value = "select l.*, c.city_name from locations l left join cities c on l.fk_city_id = c.city_id", nativeQuery = true)
  List<LocationList> findAllItem();

  @Query(value = "select l.*, c.city_name from locations l left join cities c on l.fk_city_id = c.city_id where l.location_id = ?1", nativeQuery = true)
  LocationList findLocationListById(Long id);
}
