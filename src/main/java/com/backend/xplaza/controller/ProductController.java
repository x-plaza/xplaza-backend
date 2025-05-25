/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.controller;

import java.text.ParseException;
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

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.Product;
import com.backend.xplaza.model.ProductList;
import com.backend.xplaza.model.ProductSearch;
import com.backend.xplaza.service.ProductService;
import com.backend.xplaza.service.RoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/product")
public class ProductController {
  @Autowired
  private ProductService productService;
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
  public ResponseEntity<String> getProducts(@RequestParam(value = "user_id", required = true) @Valid Long user_id)
      throws JsonProcessingException, ParseException {
    start = new Date();
    List<ProductList> dtos;
    String role_name = roleService.getRoleNameByUserID(user_id);
    if (role_name == null)
      dtos = null;
    else if (role_name.equals("Master Admin"))
      dtos = productService.listProducts();
    else
      dtos = productService.listProductsByUserID(user_id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value = { "/{id}" }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getProduct(@PathVariable @Valid Long id)
      throws JsonProcessingException, ParseException {
    start = new Date();
    ProductList dtos = productService.listProduct(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value = { "/by-shop-by-admin" }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getProductsByShopByAdmin(
      @RequestParam(value = "shop_id", required = true) @Valid Long shop_id)
      throws JsonProcessingException, ParseException {
    start = new Date();
    List<ProductList> dtos = productService.listProductsByShopIDByAdmin(shop_id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value = { "/by-shop" }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getProductsByShop(@RequestParam(value = "shop_id", required = true) @Valid Long shop_id)
      throws JsonProcessingException, ParseException {
    start = new Date();
    List<ProductList> dtos = productService.listProductsByShopID(shop_id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value = { "/by-trending" }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getProductsByTrending(
      @RequestParam(value = "shop_id", required = true) @Valid Long shop_id)
      throws JsonProcessingException, ParseException {
    start = new Date();
    List<ProductList> dtos = productService.listProductsByTrending(shop_id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value = { "/by-category" }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getProductsByCategory(
      @RequestParam(value = "shop_id", required = true) @Valid Long shop_id,
      @RequestParam(value = "category_id", required = true) @Valid Long category_id)
      throws JsonProcessingException, ParseException {
    start = new Date();
    List<ProductList> dtos = productService.listProductsByCategory(shop_id, category_id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(value = { "/by-name" }, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> searchProductsByName(
      @RequestParam(value = "shop_id", required = true) @Valid Long shop_id,
      @RequestParam(value = "product_name", required = true) @Valid String product_name)
      throws JsonProcessingException {
    start = new Date();
    List<ProductSearch> dtos = productService.listProductsByName(shop_id, product_name);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> addProduct(@RequestBody @Valid Product product)
      throws JSONException, JsonProcessingException {
    start = new Date();
    productService.addProduct(product);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Product", HttpStatus.CREATED.value(),
        "Success", "Product has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> updateProduct(@RequestBody @Valid Product product) {
    start = new Date();
    productService.updateProduct(product);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Product", HttpStatus.OK.value(),
        "Success", "Product has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ApiResponse> deleteProduct(@PathVariable @Valid Long id) {
    String product_name = productService.getProductNameByID(id);
    start = new Date();
    productService.deleteProduct(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Product", HttpStatus.OK.value(),
        "Success", product_name + " has been deleted.", null), HttpStatus.OK);
  }
}
