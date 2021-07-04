package com.backend.xplaza.service;

import com.backend.xplaza.model.Location;
import com.backend.xplaza.model.LocationList;
import com.backend.xplaza.repository.LocationListRepository;
import com.backend.xplaza.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public LocationList listLocation(long id) { return locationListRepo.findLocationListById(id); }

    public String getLocationNameByID(long id) {
        return locationRepo.getName(id);
    }

    public void deleteLocation(long id) {
        locationRepo.deleteById(id);
    }
}
