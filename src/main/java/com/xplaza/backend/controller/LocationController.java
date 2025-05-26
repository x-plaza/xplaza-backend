/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.controller;

import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.model.Location;
import com.xplaza.backend.model.LocationList;
import com.xplaza.backend.service.LocationService;

@RestController
@RequestMapping("/api/location")
public class LocationController {
  @Autowired
  private LocationService locationService;
  private Date start, end;
  private Long responseTime;

  @ModelAttribute
  public void setResponseHeader(HttpServletResponse response) {
    response.setHeader("Cache-Control", "no-store"); // HTTP 1.1.
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
    response.setHeader("Expires", "0"); // Proxies.
    response.setHeader("Content-Type", "application/json");
    response.setHeader("Set-Cookie", "type=ninja");
  }

  @GetMapping(value = { "", "/" }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getLocations() throws JsonProcessingException {
    start = new Date();
    List<LocationList> dtos = locationService.listLocations();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Location List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value = { "/{id}" }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getLocation(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    LocationList dtos = locationService.listLocation(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Location List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> addLocation(@RequestBody @Valid Location location) {
    start = new Date();
    locationService.addLocation(location);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Location", HttpStatus.CREATED.value(),
        "Success", "Location has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> updateLocation(@RequestBody @Valid Location location) {
    start = new Date();
    locationService.updateLocation(location);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Location", HttpStatus.OK.value(),
        "Success", "Location has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> deleteLocation(@PathVariable @Valid Long id) {
    String location_name = locationService.getLocationNameByID(id);
    start = new Date();
    locationService.deleteLocation(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Location", HttpStatus.OK.value(),
        "Success", location_name + " has been deleted.", null), HttpStatus.OK);
  }
}