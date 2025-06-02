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
import com.xplaza.backend.http.dto.CategoryRequestDTO;
import com.xplaza.backend.http.dto.CategoryResponseDTO;
import com.xplaza.backend.mapper.CategoryMapper;
import com.xplaza.backend.service.CategoryService;
import com.xplaza.backend.service.entity.CategoryEntity;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController extends BaseController {
  @Autowired
  private CategoryService categoryService;

  @Autowired
  private CategoryMapper categoryMapper;

  @GetMapping
  public ResponseEntity<ApiResponse> getCategories() throws Exception {
    long start = System.currentTimeMillis();
    List<CategoryEntity> categories = categoryService.listCategories();
    List<CategoryResponseDTO> dtos = categories.stream().map(categoryMapper::toResponseDTO).toList();
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dtos);
    ApiResponse response = new ApiResponse(responseTime, "Category List", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getCategory(@PathVariable @Valid Long id) throws Exception {
    long start = System.currentTimeMillis();
    CategoryEntity category = categoryService.listCategory(id);
    CategoryResponseDTO dto = categoryMapper.toResponseDTO(category);
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dto);
    ApiResponse response = new ApiResponse(responseTime, "Category By ID", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addCategory(@RequestBody @Valid CategoryRequestDTO categoryRequestDTO) {
    CategoryEntity category = categoryMapper.toEntity(categoryRequestDTO);
    categoryService.addCategory(category);
    ApiResponse response = new ApiResponse(0, "Add Category", HttpStatus.CREATED.value(), "Success",
        "Category has been created.", null);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateCategory(@RequestBody @Valid CategoryRequestDTO categoryRequestDTO) {
    CategoryEntity category = categoryMapper.toEntity(categoryRequestDTO);
    categoryService.updateCategory(category);
    ApiResponse response = new ApiResponse(0, "Update Category", HttpStatus.OK.value(), "Success",
        "Category has been updated.", null);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteCategory(@PathVariable @Valid Long id) {
    categoryService.deleteCategory(id);
    ApiResponse response = new ApiResponse(0, "Delete Category", HttpStatus.OK.value(), "Success",
        "Category has been deleted.", null);
    return ResponseEntity.ok(response);
  }
}
