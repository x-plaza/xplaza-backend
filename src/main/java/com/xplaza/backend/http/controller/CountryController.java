/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.http.dto.request.CountryRequest;
import com.xplaza.backend.http.dto.response.CountryResponse;
import com.xplaza.backend.mapper.CountryMapper;
import com.xplaza.backend.service.CountryService;
import com.xplaza.backend.service.entity.Country;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController extends BaseController {
  @Autowired
  private CountryService countryService;

  @Autowired
  private CountryMapper countryMapper;

  @GetMapping
  public ResponseEntity<ApiResponse> getCountries() throws Exception {
    long start = System.currentTimeMillis();
    List<Country> countries = countryService.listCountries();
    List<CountryResponse> dtos = countries.stream().map(countryMapper::toResponse).toList();
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dtos);
    ApiResponse response = new ApiResponse(responseTime, "Country List", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getCountry(@PathVariable @Valid Long id) throws JsonProcessingException {
    long start = System.currentTimeMillis();
    Country country = countryService.listCountry(id);
    CountryResponse dto = countryMapper.toResponse(country);
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dto);
    ApiResponse response = new ApiResponse(responseTime, "Country By ID", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addCountry(@RequestBody @Valid CountryRequest countryRequest)
      throws JsonProcessingException {
    Country country = countryMapper.toEntity(countryRequest);
    Country addedCountry = countryService.addCountry(country);
    CountryResponse dto = countryMapper.toResponse(addedCountry);
    ApiResponse response = new ApiResponse(0, "Add Country", HttpStatus.CREATED.value(), "Success",
        "Country has been created.", new ObjectMapper().writeValueAsString(dto));
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateCountry(@RequestBody @Valid CountryRequest countryRequest)
      throws JsonProcessingException {
    Country country = countryMapper.toEntity(countryRequest);
    Country updatedCountry = countryService.updateCountry(country);
    CountryResponse dto = countryMapper.toResponse(updatedCountry);
    ApiResponse response = new ApiResponse(0, "Update Country", HttpStatus.OK.value(), "Success",
        "Country has been updated.", new ObjectMapper().writeValueAsString(dto));
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteCountry(@PathVariable @Valid Long id) {
    countryService.deleteCountry(id);
    ApiResponse response = new ApiResponse(0, "Delete Country", HttpStatus.OK.value(), "Success",
        "Country has been deleted.", null);
    return ResponseEntity.ok(response);
  }
}