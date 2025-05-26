/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.controller;

import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.model.ProductDiscount;
import com.xplaza.backend.model.ProductDiscountList;
import com.xplaza.backend.service.ProductDiscountService;
import com.xplaza.backend.service.RoleService;

@RestController
@RequestMapping("/api/product-discount")
public class ProductDiscountController {
  @Autowired
  private ProductDiscountService productDiscountService;
  @Autowired
  private RoleService roleService;

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
  public ResponseEntity<String> getProductDiscounts(
      @RequestParam(value = "user_id", required = true) @Valid Long user_id) throws JsonProcessingException {
    start = new Date();
    List<ProductDiscountList> dtos;
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

  @GetMapping(value = { "/{id}" }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getProductDiscountByID(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    ProductDiscountList dtos = productDiscountService.listProductDiscount(id);
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

  @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> addProductDiscount(@RequestBody @Valid ProductDiscount productDiscount)
      throws JSONException, JsonProcessingException {
    start = new Date();
    // check if discount is greater than the product price
    if (!productDiscountService.checkDiscountValidity(productDiscount)) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Add Product Discount", HttpStatus.FORBIDDEN.value(),
          "Error", "Discount cannot be greater than the original price!", null), HttpStatus.FORBIDDEN);
    }
    // check if the product discount date is valid?
    productDiscount.setStart_date(productDiscountService.convertDateToStartOfTheDay(productDiscount.getStart_date()));
    productDiscount.setEnd_date(productDiscountService.convertDateToEndOfTheDay(productDiscount.getEnd_date()));
    if (!productDiscountService.checkDiscountDateValidity(productDiscount)) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Add Product Discount", HttpStatus.FORBIDDEN.value(),
          "Error", "Discount date is not valid! Please change discount date.", null), HttpStatus.FORBIDDEN);
    }
    productDiscountService.addProductDiscount(productDiscount);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Product Discount", HttpStatus.CREATED.value(),
        "Success", "Discount on product has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> updateProductDiscount(@RequestBody @Valid ProductDiscount productDiscount) {
    start = new Date();
    // check if discount is greater than the product price
    if (!productDiscountService.checkDiscountValidity(productDiscount)) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Update Product Discount", HttpStatus.FORBIDDEN.value(),
          "Error", "Discount cannot be greater than the original price!", null), HttpStatus.FORBIDDEN);
    }
    // check if the product discount date is valid?
    productDiscount.setStart_date(productDiscountService.convertDateToStartOfTheDay(productDiscount.getStart_date()));
    productDiscount.setEnd_date(productDiscountService.convertDateToEndOfTheDay(productDiscount.getEnd_date()));
    if (!productDiscountService.checkDiscountDateValidity(productDiscount)) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Update Product Discount", HttpStatus.FORBIDDEN.value(),
          "Error", "Discount date is not valid! Please change discount date.", null), HttpStatus.FORBIDDEN);
    }
    productDiscountService.updateProductDiscount(productDiscount);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Product Discount", HttpStatus.OK.value(),
        "Success", "Discount on product has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
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
