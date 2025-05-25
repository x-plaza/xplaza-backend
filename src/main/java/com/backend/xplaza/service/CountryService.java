/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.xplaza.model.Country;
import com.backend.xplaza.repository.CountryRepository;

@Service
public class CountryService {
  @Autowired
  private CountryRepository countryRepo;

  public void addCountry(Country country) {
    countryRepo.save(country);
  }

  public void updateCountry(Country country) {
    countryRepo.save(country);
  }

  public String getCountryNameByID(Long id) {
    return countryRepo.getName(id);
  }

  public void deleteCountry(Long id) {
    countryRepo.deleteById(id);
  }

  public List<Country> listCountries() {
    return countryRepo.findAll();
  }

  public Country listCountry(Long id) {
    return countryRepo.findCountryById(id);
  }
}
