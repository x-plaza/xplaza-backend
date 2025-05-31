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
import com.xplaza.backend.dto.CategoryRequestDTO;
import com.xplaza.backend.dto.CategoryResponseDTO;
import com.xplaza.backend.mapper.CategoryMapper;
import com.xplaza.backend.model.Category;
import com.xplaza.backend.model.CategoryList;
import com.xplaza.backend.service.CategoryService;

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
    List<CategoryList> categoryLists = categoryService.listCategories();
    List<CategoryResponseDTO> dtos = categoryLists.stream()
        .map(categoryMapper::toResponseDTO)
        .toList();
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dtos);
    ApiResponse response = new ApiResponse(responseTime, "Category List", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getCategory(@PathVariable @Valid Long id) throws Exception {
    long start = System.currentTimeMillis();
    CategoryList categoryList = categoryService.listCategory(id);
    CategoryResponseDTO dto = categoryMapper.toResponseDTO(categoryList);
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dto);
    ApiResponse response = new ApiResponse(responseTime, "Category By ID", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addCategory(@RequestBody @Valid CategoryRequestDTO categoryRequestDTO) {
    Category category = categoryMapper.toEntity(categoryRequestDTO);
    categoryService.addCategory(category);
    ApiResponse response = new ApiResponse(0, "Add Category", HttpStatus.CREATED.value(), "Success",
        "Category has been created.", null);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateCategory(@RequestBody @Valid CategoryRequestDTO categoryRequestDTO) {
    Category category = categoryMapper.toEntity(categoryRequestDTO);
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
