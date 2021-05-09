package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.Location;
import com.backend.xplaza.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/location")
public class LocationController {
    @Autowired
    private LocationService locationService;
    private Date start, end;
    long responseTime;

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Set-Cookie", "type=ninja");
        response.setHeader("msg", "");
    }

    @GetMapping(value = { "", "/" }, produces = "application/json")
    public ResponseEntity<List<Location>> getLocations() {
        start = new Date();
        List<Location> dtos = locationService.listLocations();
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("responseTime", String.valueOf(responseTime));
        responseHeader.set("responseType", "Location List");
        responseHeader.set("status", String.valueOf(HttpStatus.OK.value()));
        responseHeader.set("response", "Success");
        return new ResponseEntity<List<Location>>(dtos,responseHeader, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCountry (@RequestBody @Valid Location location) {
        locationService.addLocation(location);
        return new ResponseEntity<>(new ApiResponse(true, "Location has been created."), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCountry (@PathVariable @Valid Long id) {
        String location_name = locationService.getLocationNameByID(id);
        locationService.deleteLocation(id);
        return new ResponseEntity<>(new ApiResponse(true, location_name + " has been deleted."), HttpStatus.OK);
    }
}