/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.controller;

import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.model.ProductVariationType;
import com.xplaza.backend.service.ProductVariationTypeService;

@RestController
@RequestMapping("/api/v1/product-variation-types")
public class ProductVariationTypeController extends BaseController {
  @Autowired
  private ProductVariationTypeService prodVarTypeService;
  private Date start, end;
  private Long responseTime;

  @GetMapping
  public ResponseEntity<String> getProductVarTypes() throws JsonProcessingException {
    start = new Date();
    List<ProductVariationType> dtos = prodVarTypeService.listProductVarTypes();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product Variation Type List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getProductVarType(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    ProductVariationType dtos = prodVarTypeService.listProductVarType(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product Variation Type By ID\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addProductVarType(@RequestBody @Valid ProductVariationType productVariationType) {
    start = new Date();
    prodVarTypeService.addProductVarType(productVariationType);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Product Variation Type", HttpStatus.CREATED.value(),
        "Success", "Product Variation Type has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateProductVarType(
      @RequestBody @Valid ProductVariationType productVariationType) {
    start = new Date();
    prodVarTypeService.updateProductVarType(productVariationType);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Product Variation Type", HttpStatus.OK.value(),
        "Success", "Product Variation Type has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteProductVarType(@PathVariable @Valid Long id) {
    String prod_var_type_name = prodVarTypeService.getProductVarTypeNameByID(id);
    start = new Date();
    prodVarTypeService.deleteProductVarType(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Product Variation Type", HttpStatus.OK.value(),
        "Success", prod_var_type_name + " has been deleted.", null), HttpStatus.OK);
  }
}
