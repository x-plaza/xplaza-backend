/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.model.Location;
import com.xplaza.backend.model.LocationList;
import com.xplaza.backend.repository.LocationListRepository;
import com.xplaza.backend.repository.LocationRepository;

@Service
public class LocationService {
  @Autowired
  private LocationRepository locationRepo;

  @Autowired
  private LocationListRepository locationListRepo;

  public void addLocation(Location location) {
    locationRepo.save(location);
  }

  public void updateLocation(Location location) {
    locationRepo.save(location);
  }

  public List<LocationList> listLocations() {
    return locationListRepo.findAllItem();
  }

  public LocationList listLocation(Long id) {
    return locationListRepo.findLocationListById(id);
  }

  public String getLocationNameByID(Long id) {
    return locationRepo.getName(id);
  }

  public void deleteLocation(Long id) {
    locationRepo.deleteById(id);
  }
}
