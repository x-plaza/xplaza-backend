/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.model.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
  @Query(value = "select currency_name from currencies where currency_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from currencies where currency_id = ?1", nativeQuery = true)
  Currency findCurrencyById(Long id);
}
