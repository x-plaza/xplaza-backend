package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.Location;
import com.backend.xplaza.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @GetMapping(value = { "", "/" })
    public ResponseEntity<List<Location>> getLocations() {
        List<Location> dtos = locationService.listLocations();
        return new ResponseEntity<List<Location>>(dtos, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCountry (@RequestBody @Valid Location location) {
        locationService.addLocation(location);
        return new ResponseEntity<>(new ApiResponse(true, "Location has been created."), HttpStatus.CREATED);
    }
}