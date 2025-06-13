/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.controller;

import java.text.ParseException;
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
import com.xplaza.backend.http.dto.request.ProductRequest;
import com.xplaza.backend.http.dto.response.ProductListResponse;
import com.xplaza.backend.mapper.ProductListMapper;
import com.xplaza.backend.mapper.ProductMapper;
import com.xplaza.backend.service.ProductService;
import com.xplaza.backend.service.RoleService;
import com.xplaza.backend.service.entity.Product;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends BaseController {
  @Autowired
  private ProductService productService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private ProductMapper productMapper;

  @Autowired
  private ProductListMapper productListMapper;

  private Date start, end;
  private Long responseTime;

  @GetMapping
  public ResponseEntity<String> getProducts(@RequestParam(value = "user_id") @Valid Long user_id)
      throws JsonProcessingException, ParseException {
    start = new Date();
    List<Product> entities;
    String role_name = roleService.getRoleNameByUserID(user_id);
    if (role_name == null)
      entities = null;
    else if (role_name.equals("Master Admin"))
      entities = productService.listProducts();
    else
      entities = productService.listProductsByUserID(user_id);
    List<ProductListResponse> dtos = entities == null ? null
        : entities.stream().map(productListMapper::toResponse).toList();
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

  @GetMapping("/{id}")
  public ResponseEntity<String> getProduct(@PathVariable @Valid Long id)
      throws JsonProcessingException, ParseException {
    start = new Date();
    ProductList entity = productService.listProduct(id);
    ProductListResponse dto = productListMapper.toResponse(entity);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product By ID\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dto) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/by-shop-by-admin")
  public ResponseEntity<String> getProductsByShopByAdmin(
      @RequestParam(value = "shop_id") @Valid Long shop_id)
      throws JsonProcessingException, ParseException {
    start = new Date();
    List<ProductList> entities = productService.listProductsByShopIDByAdmin(shop_id);
    List<ProductListResponse> dtos = entities.stream().map(productListMapper::toResponse).toList();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product List By Shop By Admin\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/by-shop")
  public ResponseEntity<String> getProductsByShop(@RequestParam(value = "shop_id") @Valid Long shop_id)
      throws JsonProcessingException, ParseException {
    start = new Date();
    List<ProductList> entities = productService.listProductsByShopID(shop_id);
    List<ProductListResponse> dtos = entities.stream().map(productListMapper::toResponse).toList();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product List By Shop\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/by-trending")
  public ResponseEntity<String> getProductsByTrending(
      @RequestParam(value = "shop_id") @Valid Long shop_id)
      throws JsonProcessingException, ParseException {
    start = new Date();
    List<ProductList> entities = productService.listProductsByTrending(shop_id);
    List<ProductListResponse> dtos = entities.stream().map(productListMapper::toResponse).toList();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product List By Trending\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/by-category")
  public ResponseEntity<String> getProductsByCategory(
      @RequestParam(value = "shop_id") @Valid Long shop_id,
      @RequestParam(value = "category_id") @Valid Long category_id)
      throws JsonProcessingException, ParseException {
    start = new Date();
    List<ProductList> entities = productService.listProductsByCategory(shop_id, category_id);
    List<ProductListResponse> dtos = entities.stream().map(productListMapper::toResponse).toList();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product List By Category\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/by-name")
  public ResponseEntity<String> searchProductsByName(
      @RequestParam(value = "shop_id") @Valid Long shop_id,
      @RequestParam(value = "product_name") @Valid String product_name)
      throws JsonProcessingException {
    start = new Date();
    // This endpoint is not yet refactored to use DTO/entity separation, so return
    // empty or refactor as needed
    List<ProductList> entities = List.of(); // TODO: Implement if needed
    List<ProductListResponse> dtos = entities.stream().map(productListMapper::toResponse).toList();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Product List By Name\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addProduct(@RequestBody @Valid ProductRequest productRequest)
      throws JSONException {
    start = new Date();
    Product entity = productMapper.toEntity(productRequest);
    productService.addProduct(entity);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Product", HttpStatus.CREATED.value(),
        "Success", "Product has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateProduct(@RequestBody @Valid ProductRequest productRequest) {
    start = new Date();
    Product entity = productMapper.toEntity(productRequest);
    productService.updateProduct(entity);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Product", HttpStatus.OK.value(),
        "Success", "Product has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
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
