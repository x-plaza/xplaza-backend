/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.xplaza.model.City;
import com.backend.xplaza.repository.CityRepository;

@Service
public class CityService {
  @Autowired
  private CityRepository cityRepo;

  public void addCity(City city) {
    cityRepo.save(city);
  }

  public void updateCity(City city) {
    cityRepo.save(city);
  }

  public String getCityNameByID(Long id) {
    return cityRepo.getName(id);
  }

  public void deleteCity(Long id) {
    cityRepo.deleteById(id);
  }

  public List<City> listCities() {
    return cityRepo.findAll();
  }

  public City listCity(Long id) {
    return cityRepo.findCityById(id);
  }
}
