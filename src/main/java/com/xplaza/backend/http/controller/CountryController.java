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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.http.dto.CountryRequestDTO;
import com.xplaza.backend.http.dto.CountryResponseDTO;
import com.xplaza.backend.mapper.CountryMapper;
import com.xplaza.backend.service.CountryService;
import com.xplaza.backend.service.entity.CountryEntity;

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
    List<CountryEntity> countries = countryService.listCountries();
    List<CountryResponseDTO> dtos = countries.stream().map(countryMapper::toResponseDTO).toList();
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dtos);
    ApiResponse response = new ApiResponse(responseTime, "Country List", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getCountry(@PathVariable @Valid Long id) throws Exception {
    long start = System.currentTimeMillis();
    CountryEntity country = countryService.listCountry(id);
    CountryResponseDTO dto = countryMapper.toResponseDTO(country);
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dto);
    ApiResponse response = new ApiResponse(responseTime, "Country By ID", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addCountry(@RequestBody @Valid CountryRequestDTO countryRequestDTO) {
    CountryEntity country = countryMapper.toEntity(countryRequestDTO);
    countryService.addCountry(country);
    ApiResponse response = new ApiResponse(0, "Add Country", HttpStatus.CREATED.value(), "Success",
        "Country has been created.", null);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateCountry(@RequestBody @Valid CountryRequestDTO countryRequestDTO) {
    CountryEntity country = countryMapper.toEntity(countryRequestDTO);
    countryService.updateCountry(country);
    ApiResponse response = new ApiResponse(0, "Update Country", HttpStatus.OK.value(), "Success",
        "Country has been updated.", null);
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