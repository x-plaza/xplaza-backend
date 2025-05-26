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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.model.DeliveryCost;
import com.xplaza.backend.model.DeliveryCostList;
import com.xplaza.backend.service.DeliveryCostService;

@RestController
@RequestMapping("/api/v1/delivery-costs")
public class DeliveryCostController {
  @Autowired
  private DeliveryCostService deliveryCostService;

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

  @GetMapping
  public ResponseEntity<String> getDeliveryCosts() throws JsonProcessingException, JSONException {
    start = new Date();
    List<DeliveryCostList> dtos = deliveryCostService.listDeliveryCosts();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Delivery Cost List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getDeliveryCost(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    DeliveryCostList dtos = deliveryCostService.listDeliveryCost(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Delivery Cost By ID\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addDeliveryCost(@RequestBody @Valid DeliveryCost deliveryCost) {
    start = new Date();
    deliveryCostService.addDeliveryCost(deliveryCost);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Delivery Cost", HttpStatus.CREATED.value(),
        "Success", "Delivery Cost has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateDeliveryCost(@RequestBody @Valid DeliveryCost deliveryCost) {
    start = new Date();
    deliveryCostService.updateDeliveryCost(deliveryCost);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Delivery Cost", HttpStatus.OK.value(),
        "Success", "Delivery Cost has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteDeliveryCost(@PathVariable @Valid Long id) {
    String delivery_slab = deliveryCostService.getDeliverySlabRangeNameByID(id);
    start = new Date();
    deliveryCostService.deleteDeliveryCost(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Delivery Cost", HttpStatus.OK.value(),
        "Success", "Delivery cost of " + delivery_slab + " order range has been deleted.", null), HttpStatus.OK);
  }
}
