/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.CityDao;

public interface CityRepository extends JpaRepository<CityDao, Long> {
  @Query(value = "select city_name from cities where city_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from cities where city_id = ?1", nativeQuery = true)
  CityDao findCityById(Long id);

  @Query(value = "SELECT * FROM cities WHERE state_id = ?1", nativeQuery = true)
  List<CityDao> findByStateId(Long stateId);

  @Query(value = "SELECT * FROM cities WHERE country_id = ?1", nativeQuery = true)
  List<CityDao> findByCountryId(Long countryId);
}
