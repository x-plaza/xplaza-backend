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
import com.xplaza.backend.model.Category;
import com.xplaza.backend.model.CategoryList;
import com.xplaza.backend.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

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
  public ResponseEntity<String> getCategories() throws JsonProcessingException {
    start = new Date();
    List<CategoryList> dtos = categoryService.listCategories();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Category List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value = { "/{id}" }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getCategory(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    CategoryList dtos = categoryService.listCategory(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Category List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> addCategory(@RequestBody @Valid Category category) {
    start = new Date();
    // check if same category already exists?
    if (categoryService.isExist(category)) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Add Category", HttpStatus.FORBIDDEN.value(),
          "Error", "Category already exists! Please use different category name.", null), HttpStatus.FORBIDDEN);
    }
    categoryService.addCategory(category);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Category", HttpStatus.CREATED.value(),
        "Success", "Category has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> updateCategory(@RequestBody @Valid Category category) {
    start = new Date();
    categoryService.updateCategory(category);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Category", HttpStatus.OK.value(),
        "Success", "Category has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> deleteCategory(@PathVariable @Valid Long id) {
    String category_name = categoryService.getCategoryNameByID(id);
    start = new Date();
    // check if a category has still child category?
    if (categoryService.hasChildCategory(id)) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Category", HttpStatus.FORBIDDEN.value(),
          "Error", "Cannot delete " + category_name + ". It still has child category.", null), HttpStatus.FORBIDDEN);
    }
    categoryService.deleteCategory(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Category", HttpStatus.OK.value(),
        "Success", category_name + " has been deleted.", null), HttpStatus.OK);
  }
}
