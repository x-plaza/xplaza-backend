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
import com.xplaza.backend.mapper.ShopMapper;
import com.xplaza.backend.model.ShopList;
import com.xplaza.backend.service.RoleService;
import com.xplaza.backend.service.ShopService;
import com.xplaza.backend.service.entity.ShopEntity;

@RestController
@RequestMapping("/api/v1/shops")
public class ShopController extends BaseController {
  @Autowired
  private ShopService shopService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private ShopMapper shopMapper;

  private Date start, end;
  private Long responseTime;

  @GetMapping
  public ResponseEntity<ApiResponse> getShops() throws Exception {
    long start = System.currentTimeMillis();
    List<ShopEntity> shops = shopService.listShops();
    List<ShopResponseDTO> dtos = shops.stream().map(shopMapper::toResponseDTO).toList();
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dtos);
    ApiResponse response = new ApiResponse(responseTime, "Shop List", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getShop(@PathVariable @Valid Long id) throws Exception {
    long start = System.currentTimeMillis();
    ShopEntity shop = shopService.listShop(id);
    ShopResponseDTO dto = shopMapper.toResponseDTO(shop);
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dto);
    ApiResponse response = new ApiResponse(responseTime, "Shop By ID", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/by-location/{id}")
  public ResponseEntity<String> getShopsByLocation(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    List<ShopList> dtos = shopService.listShopByLocation(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Shop List By Location\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addShop(@RequestBody @Valid ShopRequestDTO shopRequestDTO) {
    ShopEntity shop = shopMapper.toEntity(shopRequestDTO);
    shopService.addShop(shop);
    ApiResponse response = new ApiResponse(0, "Add Shop", HttpStatus.CREATED.value(), "Success",
        "Shop has been created.", null);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateShop(@RequestBody @Valid ShopRequestDTO shopRequestDTO) {
    ShopEntity shop = shopMapper.toEntity(shopRequestDTO);
    shopService.updateShop(shop);
    ApiResponse response = new ApiResponse(0, "Update Shop", HttpStatus.OK.value(), "Success",
        "Shop has been updated.", null);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteShop(@PathVariable @Valid Long id) {
    shopService.deleteShop(id);
    ApiResponse response = new ApiResponse(0, "Delete Shop", HttpStatus.OK.value(), "Success",
        "Shop has been deleted.", null);
    return ResponseEntity.ok(response);
  }
}
