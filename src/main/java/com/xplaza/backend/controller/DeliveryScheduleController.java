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
import com.xplaza.backend.model.DeliverySchedule;
import com.xplaza.backend.model.DeliveryScheduleDetails;
import com.xplaza.backend.model.DeliveryScheduleList;
import com.xplaza.backend.service.DeliveryScheduleService;

@RestController
@RequestMapping("/api/v1/delivery-schedule")
public class DeliveryScheduleController {
  @Autowired
  private DeliveryScheduleService deliveryScheduleService;

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
  public ResponseEntity<String> getDeliverySchedules() throws JsonProcessingException {
    start = new Date();
    List<DeliveryScheduleList> dtos = deliveryScheduleService.listDeliverySchedules();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Delivery Schedule List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getDeliverySchedule(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    DeliveryScheduleDetails dtos = deliveryScheduleService.listDeliveryScheduleDetails(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Delivery Schedule By ID\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addSchedule(@RequestBody @Valid DeliverySchedule deliverySchedule) {
    start = new Date();
    deliveryScheduleService.addSchedule(deliverySchedule);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Delivery Schedule", HttpStatus.CREATED.value(),
        "Success", "Delivery Schedule has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateSchedule(@RequestBody @Valid DeliverySchedule deliverySchedule) {
    start = new Date();
    deliveryScheduleService.updateSchedule(deliverySchedule);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Delivery Schedule", HttpStatus.OK.value(),
        "Success", "Delivery Schedule has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteSchedule(@PathVariable @Valid Long id) {
    start = new Date();
    deliveryScheduleService.deleteSchedule(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Delivery Schedule", HttpStatus.OK.value(),
        "Success", "Delivery Schedule has been deleted.", null), HttpStatus.OK);
  }

}
