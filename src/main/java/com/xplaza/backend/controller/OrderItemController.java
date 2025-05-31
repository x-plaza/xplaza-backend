/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.controller;

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
import com.xplaza.backend.model.OrderItem;
import com.xplaza.backend.service.OrderItemService;

@RestController
@RequestMapping("/api/v1/order-items")
public class OrderItemController extends BaseController {
  @Autowired
  private OrderItemService orderItemService;

  private Date start, end;
  private Long responseTime;

  @GetMapping
  public ResponseEntity<String> getOrderItems() throws JsonProcessingException, JSONException {
    start = new Date();
    List<OrderItem> dtos = orderItemService.listOrderItems();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Order Item List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getOrderItem(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    OrderItem dtos = orderItemService.listOrderItem(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Order Item By ID\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addOrderItem(@RequestBody @Valid OrderItem orderItem) {
    start = new Date();
    orderItemService.addOrderItem(orderItem);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Order Item", HttpStatus.CREATED.value(),
        "Success", "Order Item has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateOrderItem(@RequestParam("order_item_id") @Valid Long order_item_id,
      @RequestParam("quantity") @Valid Long quantity) {
    start = new Date();
    orderItemService.updateOrderItem(order_item_id, quantity);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Order Item", HttpStatus.OK.value(),
        "Success", "Order Item has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteOrderItem(@PathVariable @Valid Long id) {
    String order_item_name = orderItemService.getOrderItemNameByID(id);
    start = new Date();
    orderItemService.deleteOrderItem(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Order Item", HttpStatus.OK.value(),
        "Success", order_item_name + " has been deleted.", null), HttpStatus.OK);
  }
}
