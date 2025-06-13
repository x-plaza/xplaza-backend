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
import com.xplaza.backend.http.dto.request.BrandRequest;
import com.xplaza.backend.http.dto.response.BrandResponse;
import com.xplaza.backend.mapper.BrandMapper;
import com.xplaza.backend.service.BrandService;
import com.xplaza.backend.service.entity.Brand;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandController extends BaseController {
  @Autowired
  private BrandService brandService;

  @Autowired
  private BrandMapper brandMapper;

  @GetMapping
  public ResponseEntity<ApiResponse> getBrands() throws Exception {
    long start = System.currentTimeMillis();
    List<Brand> brands = brandService.listBrands();
    List<BrandResponse> dtos = brands.stream().map(brandMapper::toResponse).toList();
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dtos);
    ApiResponse response = new ApiResponse(responseTime, "Brand List", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getBrand(@PathVariable @Valid Long id) throws Exception {
    long start = System.currentTimeMillis();
    Brand brand = brandService.listBrand(id);
    BrandResponse dto = brandMapper.toResponse(brand);
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dto);
    ApiResponse response = new ApiResponse(responseTime, "Brand By ID", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addBrand(@RequestBody @Valid BrandRequest brandRequest) {
    Brand brand = brandMapper.toEntity(brandRequest);
    brandService.addBrand(brand);
    ApiResponse response = new ApiResponse(0, "Add Brand", HttpStatus.CREATED.value(), "Success",
        "Brand has been created.", null);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateBrand(@RequestBody @Valid BrandRequest brandRequest) {
    Brand brand = brandMapper.toEntity(brandRequest);
    brandService.updateBrand(brand);
    ApiResponse response = new ApiResponse(0, "Update Brand", HttpStatus.OK.value(), "Success",
        "Brand has been updated.", null);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteBrand(@PathVariable @Valid Long id) {
    brandService.deleteBrand(id);
    ApiResponse response = new ApiResponse(0, "Delete Brand", HttpStatus.OK.value(), "Success",
        "Brand has been deleted.", null);
    return ResponseEntity.ok(response);
  }
}
