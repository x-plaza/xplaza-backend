/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.repository.CountryRepository;
import com.xplaza.backend.mapper.CountryMapper;
import com.xplaza.backend.service.entity.Country;

@Service
public class CountryService {
  @Autowired
  private CountryRepository countryRepository;

  @Autowired
  private CountryMapper countryMapper;

  public void addCountry(Country entity) {
    countryRepository.save(countryMapper.toDao(entity));
  }

  public void updateCountry(Country entity) {
    countryRepository.save(countryMapper.toDao(entity));
  }

  public void deleteCountry(Long id) {
    countryRepository.deleteById(id);
  }

  public Country listCountry(Long id) {
    return countryRepository.findById(id).map(countryMapper::toEntity).orElse(null);
  }

  public List<Country> listCountries() {
    return countryRepository.findAll().stream().map(countryMapper::toEntity).toList();
  }
}
