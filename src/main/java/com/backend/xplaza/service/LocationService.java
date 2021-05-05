package com.backend.xplaza.service;

import com.backend.xplaza.model.Location;
import com.backend.xplaza.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepo;

    public void addLocation(Location location) {
        locationRepo.save(location);
    }

    public List<Location> listLocations() {
        return locationRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }
}
