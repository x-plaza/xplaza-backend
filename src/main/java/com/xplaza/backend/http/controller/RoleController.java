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
import com.xplaza.backend.http.dto.request.RoleRequest;
import com.xplaza.backend.http.dto.response.RoleResponse;
import com.xplaza.backend.mapper.RoleMapper;
import com.xplaza.backend.service.RoleService;
import com.xplaza.backend.service.entity.Role;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController extends BaseController {
  @Autowired
  private RoleService roleService;
  @Autowired
  private RoleMapper roleMapper;

  private Date start, end;
  private Long responseTime;

  @GetMapping
  public ResponseEntity<String> getRoles() throws JsonProcessingException {
    start = new Date();
    List<Role> roles = roleService.listRoles();
    roles.removeIf(r -> r.getRoleName().equals("Master Admin"));
    List<RoleResponse> dtos = roles.stream().map(roleMapper::toResponse).toList();
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Role List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getRole(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    Role entity = roleService.listRole(id);
    RoleResponse dto = roleMapper.toResponse(entity);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Role by ID\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dto) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addRole(@RequestBody @Valid RoleRequest roleRequest) {
    start = new Date();
    Role entity = roleMapper.toEntity(roleRequest);
    roleService.addRole(entity);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Role", HttpStatus.CREATED.value(),
        "Success", "Role has been created.", null), HttpStatus.CREATED);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateRole(@RequestBody @Valid RoleRequest roleRequestDTO) {
    start = new Date();
    Role entity = roleMapper.toEntity(roleRequestDTO);
    roleService.updateRole(entity);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Role", HttpStatus.OK.value(),
        "Success", "Role has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteRole(@PathVariable @Valid Long id) {
    String role_name = roleService.getRoleNameByID(id);
    start = new Date();
    roleService.deleteRole(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Role", HttpStatus.OK.value(),
        "Success", role_name + " has been deleted.", null), HttpStatus.OK);
  }
}
