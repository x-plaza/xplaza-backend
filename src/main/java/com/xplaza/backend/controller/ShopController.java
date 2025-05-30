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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.model.Shop;
import com.xplaza.backend.model.ShopList;
import com.xplaza.backend.service.RoleService;
import com.xplaza.backend.service.ShopService;

@RestController
@RequestMapping("/api/v1/shops")
public class ShopController {
  @Autowired
  private ShopService shopService;

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
    response.setHeader("msg", "");
  }

  @GetMapping
  public ResponseEntity<String> getShops(@RequestParam(value = "user_id") @Valid Long user_id)
      throws JsonProcessingException {
    start = new Date();
    List<ShopList> dtos;
    String role_name = roleService.getRoleNameByUserID(user_id);
    if (role_name == null)
      dtos = null;
    else if (role_name.equals("Master Admin"))
      dtos = shopService.listShops();
    else
      dtos = shopService.listShopsByUserID(user_id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Shop List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getShop(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    ShopList dtos = shopService.listShop(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Shop by ID\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
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
  public ResponseEntity<ApiResponse> addShop(@RequestBody @Valid Shop shop) {
    start = new Date();
    shopService.addShop(shop);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Shop", HttpStatus.CREATED.value(),
        "Success", "Shop has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateShop(@RequestBody @Valid Shop shop) {
    start = new Date();
    shopService.updateShop(shop);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Shop", HttpStatus.OK.value(),
        "Success", "Shop has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteShop(@PathVariable @Valid Long id) {
    String shop_name = shopService.getShopNameByID(id);
    start = new Date();
    shopService.deleteShop(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Shop", HttpStatus.OK.value(),
        "Success", shop_name + " has been deleted.", null), HttpStatus.OK);
  }
}
