/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.controller;

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
import com.xplaza.backend.model.Day;
import com.xplaza.backend.service.DayNameService;

@RestController
@RequestMapping("/api/v1/day-names")
public class DayNameController extends BaseController {
  @Autowired
  private DayNameService dayNameService;

  private Date start, end;
  private Long responseTime;

  @GetMapping
  public ResponseEntity<String> getDays() throws JsonProcessingException {
    start = new Date();
    List<Day> dtos = dayNameService.listDays();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Day List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getDay(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    String dayName = dayNameService.getDayNameByID(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Day By ID\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dayName) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addDay(@RequestBody @Valid Day day) {
    start = new Date();
    dayNameService.addDay(day);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Day", HttpStatus.CREATED.value(),
        "Success", "Day has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateDay(@RequestBody @Valid Day day) {
    start = new Date();
    dayNameService.updateDay(day);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Day", HttpStatus.OK.value(),
        "Success", "Day has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteDay(@PathVariable @Valid Long id) {
    String day_name = dayNameService.getDayNameByID(id);
    start = new Date();
    dayNameService.deleteDay(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Day", HttpStatus.OK.value(),
        "Success", day_name + " has been deleted.", null), HttpStatus.OK);
  }
}
