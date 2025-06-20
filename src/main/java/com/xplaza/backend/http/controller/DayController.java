/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.controller;

import java.util.Date;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.http.dto.request.DayRequest;
import com.xplaza.backend.mapper.DayMapper;
import com.xplaza.backend.service.DayService;
import com.xplaza.backend.service.entity.Day;

@RestController
@RequestMapping("/api/v1/day-names")
public class DayController extends BaseController {
  private final DayService dayService;
  private final DayMapper dayMapper;

  @Autowired
  public DayController(DayService dayService, DayMapper dayMapper) {
    this.dayService = dayService;
    this.dayMapper = dayMapper;
  }

  private Date start, end;
  private Long responseTime;

  @GetMapping
  public ResponseEntity<ApiResponse> getDays() throws JsonProcessingException {
    start = new Date();
    var entities = dayService.listDays();
    var dtos = entities.stream().map(dayMapper::toResponse).toList();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    String data = new ObjectMapper().writeValueAsString(dtos);
    ApiResponse response = new ApiResponse(responseTime, "DayName List", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getDay(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    String dayName = dayService.getDayNameByID(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    String data = new ObjectMapper().writeValueAsString(dayName);
    ApiResponse response = new ApiResponse(responseTime, "DayName By ID", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addDay(@RequestBody @Valid DayRequest dayRequest) {
    start = new Date();
    Day entity = dayMapper.toEntity(dayRequest);
    dayService.addDay(entity);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add DayName", HttpStatus.CREATED.value(),
        "Success", "DayName has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateDay(@RequestBody @Valid DayRequest dayRequest) {
    start = new Date();
    Day entity = dayMapper.toEntity(dayRequest);
    dayService.updateDay(entity);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update DayName", HttpStatus.OK.value(),
        "Success", "DayName has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteDay(@PathVariable @Valid Long id) {
    String day_name = dayService.getDayNameByID(id);
    start = new Date();
    dayService.deleteDay(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete DayName", HttpStatus.OK.value(),
        "Success", day_name + " has been deleted.", null), HttpStatus.OK);
  }
}
