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
import com.xplaza.backend.model.PlatformInfo;
import com.xplaza.backend.service.PlatformInfoService;

@RestController
@RequestMapping("/api/v1/platform-infos")
public class PlatformInfoController extends BaseController {
  @Autowired
  PlatformInfoService platformInfoService;

  private Date start, end;
  private Long responseTime;

  @GetMapping
  public ResponseEntity<String> getPlatformInfo() throws JsonProcessingException {
    start = new Date();
    PlatformInfo dtos = platformInfoService.listPlatform();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Platform Info\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updatePlatformInfo(@RequestBody @Valid PlatformInfo platformInfo) {
    start = new Date();
    platformInfoService.updatePlatformInfo(platformInfo);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Platform Info", HttpStatus.OK.value(),
        "Success", "Platform Info has been updated.", null), HttpStatus.OK);
  }
}
