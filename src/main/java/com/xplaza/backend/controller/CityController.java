/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.dto.CityRequestDTO;
import com.xplaza.backend.dto.CityResponseDTO;
import com.xplaza.backend.service.CityService;

@RestController
@RequestMapping("/api/v1/cities")
public class CityController extends BaseController {
  @Autowired
  private CityService cityService;

  @GetMapping
  public ResponseEntity<ApiResponse> getCities() throws Exception {
    long start = System.currentTimeMillis();
    List<CityResponseDTO> dtos = cityService.listCities();
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dtos);
    ApiResponse response = new ApiResponse(responseTime, "City List", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getCity(@PathVariable @Valid Long id) throws Exception {
    long start = System.currentTimeMillis();
    CityResponseDTO dto = cityService.listCity(id);
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dto);
    ApiResponse response = new ApiResponse(responseTime, "City By ID", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addCity(@RequestBody @Valid CityRequestDTO cityRequestDTO) {
    cityService.addCity(cityRequestDTO);
    ApiResponse response = new ApiResponse(0, "Add City", HttpStatus.CREATED.value(), "Success",
        "City has been created.", null);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateCity(@RequestBody @Valid CityRequestDTO cityRequestDTO) {
    cityService.updateCity(cityRequestDTO);
    ApiResponse response = new ApiResponse(0, "Update City", HttpStatus.OK.value(), "Success", "City has been updated.",
        null);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteCity(@PathVariable @Valid Long id) {
    cityService.deleteCity(id);
    ApiResponse response = new ApiResponse(0, "Delete City", HttpStatus.OK.value(), "Success", "City has been deleted.",
        null);
    return ResponseEntity.ok(response);
  }
}
