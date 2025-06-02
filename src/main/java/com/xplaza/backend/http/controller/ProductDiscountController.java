/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.controller;

import java.util.Date;
import java.util.List;

import jakarta.validation.Valid;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.http.dto.ProductDiscountRequestDTO;
import com.xplaza.backend.http.dto.ProductDiscountResponseDTO;
import com.xplaza.backend.service.ProductDiscountService;
import com.xplaza.backend.service.RoleService;

@RestController
@RequestMapping("/api/v1/product-discounts")
public class ProductDiscountController extends BaseController {
  @Autowired
  private ProductDiscountService productDiscountService;

  @Autowired
  private RoleService roleService;

  private Date start, end;
  private Long responseTime;

  @GetMapping
  public ResponseEntity<String> getProductDiscounts(
      @RequestParam(value = "user_id") @Valid Long user_id) throws JsonProcessingException {
    start = new Date();
    List<ProductDiscountResponseDTO> dtos;
    String role_name = roleService.getRoleNameByUserID(user_id);
    if (role_name == null)
      dtos = null;
    else if (role_name.equals("Master Admin"))
      dtos = productDiscountService.listProductDiscounts();
    else
      dtos = productDiscountService.listProductDiscountsByUserID(user_id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product Discount List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getProductDiscountByID(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    ProductDiscountResponseDTO dto = productDiscountService.listProductDiscount(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product Discount By ID\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dto) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addProductDiscount(
      @RequestBody @Valid ProductDiscountRequestDTO productDiscountRequestDTO)
      throws JSONException {
    start = new Date();
    if (!productDiscountService.checkDiscountValidity(productDiscountRequestDTO)) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Add Product Discount", HttpStatus.FORBIDDEN.value(),
          "Error", "Discount cannot be greater than the original price!", null), HttpStatus.FORBIDDEN);
    }
    if (!productDiscountService.checkDiscountDateValidity(productDiscountRequestDTO)) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Add Product Discount", HttpStatus.FORBIDDEN.value(),
          "Error", "Discount date is not valid! Please change discount date.", null), HttpStatus.FORBIDDEN);
    }
    productDiscountService.addProductDiscount(productDiscountRequestDTO);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Product Discount", HttpStatus.CREATED.value(),
        "Success", "Discount on product has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateProductDiscount(
      @RequestBody @Valid ProductDiscountRequestDTO productDiscountRequestDTO) {
    start = new Date();
    if (!productDiscountService.checkDiscountValidity(productDiscountRequestDTO)) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Update Product Discount", HttpStatus.FORBIDDEN.value(),
          "Error", "Discount cannot be greater than the original price!", null), HttpStatus.FORBIDDEN);
    }
    if (!productDiscountService.checkDiscountDateValidity(productDiscountRequestDTO)) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Update Product Discount", HttpStatus.FORBIDDEN.value(),
          "Error", "Discount date is not valid! Please change discount date.", null), HttpStatus.FORBIDDEN);
    }
    productDiscountService.updateProductDiscount(productDiscountRequestDTO);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Product Discount", HttpStatus.OK.value(),
        "Success", "Discount on product has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteProductDiscount(@PathVariable @Valid Long id) {
    String product_name = productDiscountService.getProductNameByID(id);
    start = new Date();
    productDiscountService.deleteProductDiscount(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Product Discount", HttpStatus.OK.value(),
        "Success", "Discount on " + product_name + " has been deleted.", null), HttpStatus.OK);
  }
}
