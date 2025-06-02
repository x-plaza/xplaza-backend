/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.http.dto.ModuleRequestDTO;
import com.xplaza.backend.http.dto.ModuleResponseDTO;
import com.xplaza.backend.mapper.ModuleMapper;
import com.xplaza.backend.service.ModuleService;
import com.xplaza.backend.service.entity.ModuleEntity;

@RestController
@RequestMapping("/api/v1/modules")
public class ModuleController extends BaseController {
  @Autowired
  private ModuleService moduleService;

  @Autowired
  private ModuleMapper moduleMapper;

  @GetMapping
  public ResponseEntity<ApiResponse> getModules() throws JsonProcessingException {
    long start = System.currentTimeMillis();
    List<ModuleEntity> modules = moduleService.listModules();
    List<ModuleResponseDTO> dtos = modules.stream().map(moduleMapper::toResponseDTO).toList();
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dtos);
    ApiResponse response = new ApiResponse(responseTime, "Module List", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse> getModule(@PathVariable @Valid Long id) throws JsonProcessingException {
    long start = System.currentTimeMillis();
    ModuleEntity module = moduleService.listModule(id);
    ModuleResponseDTO dto = moduleMapper.toResponseDTO(module);
    long end = System.currentTimeMillis();
    long responseTime = end - start;
    String data = new ObjectMapper().writeValueAsString(dto);
    ApiResponse response = new ApiResponse(responseTime, "Module By ID", HttpStatus.OK.value(), "Success", "", data);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addModule(@RequestBody @Valid ModuleRequestDTO moduleRequestDTO) {
    ModuleEntity module = moduleMapper.toEntity(moduleRequestDTO);
    moduleService.addModule(module);
    ApiResponse response = new ApiResponse(0, "Add Module", HttpStatus.CREATED.value(), "Success",
        "Module has been created.", null);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateModule(@RequestBody @Valid ModuleRequestDTO moduleRequestDTO) {
    ModuleEntity module = moduleMapper.toEntity(moduleRequestDTO);
    moduleService.updateModule(module);
    ApiResponse response = new ApiResponse(0, "Update Module", HttpStatus.OK.value(), "Success",
        "Module has been updated.", null);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteModule(@PathVariable @Valid Long id) {
    String module_name = moduleService.getModuleNameByID(id);
    moduleService.deleteModule(id);
    ApiResponse response = new ApiResponse(0, "Delete Module", HttpStatus.OK.value(), "Success",
        module_name + " has been deleted.", null);
    return ResponseEntity.ok(response);
  }
}
