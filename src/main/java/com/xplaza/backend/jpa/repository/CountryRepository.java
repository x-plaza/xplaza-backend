/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.CountryDao;

public interface CountryRepository extends JpaRepository<CountryDao, Long> {
  @Query(value = "select country_name from countries where country_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from countries where country_id = ?1", nativeQuery = true)
  CountryDao findCountryById(Long id);
}
