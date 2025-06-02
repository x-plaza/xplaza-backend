/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.repository.CityRepository;
import com.xplaza.backend.mapper.CityMapper;
import com.xplaza.backend.service.entity.City;

@Service
public class CityService {
  @Autowired
  private CityRepository cityRepository;

  @Autowired
  private CityMapper cityMapper;

  public void addCity(City entity) {
    cityRepository.save(entity);
  }

  public void updateCity(City entity) {
    cityRepository.save(entity);
  }

  public void deleteCity(Long id) {
    cityRepository.deleteById(id);
  }

  public City listCity(Long id) {
    return cityRepository.findById(id).orElse(null);
  }

  public List<City> listCities() {
    return cityRepository.findAll();
  }
}
