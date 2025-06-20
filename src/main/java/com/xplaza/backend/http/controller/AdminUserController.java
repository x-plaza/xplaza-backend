/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.http.dto.request.AdminUserRequest;
import com.xplaza.backend.http.dto.response.AdminUserResponse;
import com.xplaza.backend.mapper.AdminUserMapper;
import com.xplaza.backend.service.AdminUserLoginService;
import com.xplaza.backend.service.AdminUserService;
import com.xplaza.backend.service.ConfirmationTokenService;
import com.xplaza.backend.service.RoleService;
import com.xplaza.backend.service.SecurityService;
import com.xplaza.backend.service.entity.AdminUser;

@RestController
@RequestMapping("/api/v1/admin-users")
@Tag(name = "Admin User Management", description = "APIs for managing admin users")
public class AdminUserController extends BaseController {
  private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);
  private final AdminUserService adminUserService;
  private final AdminUserLoginService adminUserLoginService;
  private final ConfirmationTokenService confirmationTokenService;
  private final SecurityService securityService;
  private final RoleService roleService;
  private final AdminUserMapper adminUserMapper;
  private final ObjectMapper objectMapper;

  @Autowired
  public AdminUserController(AdminUserService adminUserService, AdminUserLoginService adminUserLoginService,
      ConfirmationTokenService confirmationTokenService, SecurityService securityService, RoleService roleService,
      AdminUserMapper adminUserMapper, ObjectMapper objectMapper) {
    this.adminUserService = adminUserService;
    this.adminUserLoginService = adminUserLoginService;
    this.confirmationTokenService = confirmationTokenService;
    this.securityService = securityService;
    this.roleService = roleService;
    this.adminUserMapper = adminUserMapper;
    this.objectMapper = objectMapper;
  }

  @GetMapping
  @Operation(summary = "Get all admin users", description = "Retrieves a list of all admin users in the system", responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved admin users", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<ApiResponse> getAllAdminUsers() {
    try {
      List<AdminUser> adminUsers = adminUserService.listAdminUsers();
      List<AdminUserResponse> responses = adminUsers.stream()
          .map(adminUserMapper::toResponse)
          .collect(Collectors.toList());

      ApiResponse response = new ApiResponse(
          System.currentTimeMillis(),
          "success",
          200,
          "Admin users retrieved successfully",
          "Admin users retrieved successfully",
          objectMapper.writeValueAsString(responses));

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      logger.error("Error retrieving admin users", e);
      ApiResponse response = new ApiResponse(
          System.currentTimeMillis(),
          "error",
          500,
          "Failed to retrieve admin users",
          "Failed to retrieve admin users: " + e.getMessage(),
          null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get admin user by ID", description = "Retrieves a specific admin user by their ID", parameters = {
      @Parameter(name = "id", description = "Admin user ID", required = true, example = "1")
  }, responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved admin user", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Admin user not found"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<ApiResponse> getAdminUserById(@PathVariable Long id) {
    try {
      AdminUser adminUser = adminUserService.listAdminUser(id);
      if (adminUser == null) {
        ApiResponse response = new ApiResponse(
            System.currentTimeMillis(),
            "error",
            404,
            "Admin user not found",
            "Admin user not found",
            null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
      }

      AdminUserResponse response = adminUserMapper.toResponse(adminUser);

      ApiResponse apiResponse = new ApiResponse(
          System.currentTimeMillis(),
          "success",
          200,
          "Admin user retrieved successfully",
          "Admin user retrieved successfully",
          objectMapper.writeValueAsString(response));

      return ResponseEntity.ok(apiResponse);
    } catch (Exception e) {
      logger.error("Error retrieving admin user with id: {}", id, e);
      ApiResponse response = new ApiResponse(
          System.currentTimeMillis(),
          "error",
          500,
          "Failed to retrieve admin user",
          "Failed to retrieve admin user: " + e.getMessage(),
          null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @PostMapping
  @Operation(summary = "Create new admin user", description = "Creates a new admin user in the system", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Admin user data", required = true, content = @Content(schema = @Schema(implementation = AdminUserRequest.class))), responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Admin user created successfully", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<ApiResponse> createAdminUser(@Valid @RequestBody AdminUserRequest request) {
    try {
      AdminUser adminUser = adminUserMapper.toEntity(request);
      adminUserService.addAdminUser(adminUser);
      AdminUserResponse response = adminUserMapper.toResponse(adminUser);

      ApiResponse apiResponse = new ApiResponse(
          System.currentTimeMillis(),
          "success",
          201,
          "Admin user created successfully",
          "Admin user created successfully",
          objectMapper.writeValueAsString(response));

      return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    } catch (Exception e) {
      logger.error("Error creating admin user", e);
      ApiResponse response = new ApiResponse(
          System.currentTimeMillis(),
          "error",
          500,
          "Failed to create admin user",
          "Failed to create admin user: " + e.getMessage(),
          null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update admin user", description = "Updates an existing admin user by ID", parameters = {
      @Parameter(name = "id", description = "Admin user ID", required = true, example = "1")
  }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated admin user data", required = true, content = @Content(schema = @Schema(implementation = AdminUserRequest.class))), responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Admin user updated successfully", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Admin user not found"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<ApiResponse> updateAdminUser(@PathVariable Long id,
      @Valid @RequestBody AdminUserRequest request) {
    try {
      AdminUser adminUser = adminUserMapper.toEntity(request);
      adminUser.setAdminUserId(id);
      adminUserService.updateAdminUser(adminUser);
      AdminUserResponse response = adminUserMapper.toResponse(adminUser);

      ApiResponse apiResponse = new ApiResponse(
          System.currentTimeMillis(),
          "success",
          200,
          "Admin user updated successfully",
          "Admin user updated successfully",
          objectMapper.writeValueAsString(response));

      return ResponseEntity.ok(apiResponse);
    } catch (Exception e) {
      logger.error("Error updating admin user with id: {}", id, e);
      ApiResponse response = new ApiResponse(
          System.currentTimeMillis(),
          "error",
          500,
          "Failed to update admin user",
          "Failed to update admin user: " + e.getMessage(),
          null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete admin user", description = "Deletes an admin user by ID", parameters = {
      @Parameter(name = "id", description = "Admin user ID", required = true, example = "1")
  }, responses = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Admin user deleted successfully", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Admin user not found"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<ApiResponse> deleteAdminUser(@PathVariable Long id) {
    try {
      adminUserService.deleteAdminUser(id);

      ApiResponse response = new ApiResponse(
          System.currentTimeMillis(),
          "success",
          200,
          "Admin user deleted successfully",
          "Admin user deleted successfully",
          null);

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      logger.error("Error deleting admin user with id: {}", id, e);
      ApiResponse response = new ApiResponse(
          System.currentTimeMillis(),
          "error",
          500,
          "Failed to delete admin user",
          "Failed to delete admin user: " + e.getMessage(),
          null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  // Keep existing methods for backward compatibility
  @GetMapping("/legacy")
  public ResponseEntity<?> getAdminUsers(@RequestParam(value = "user_id") @Valid Long userId) {
    String roleName = roleService.getRoleNameByUserID(userId);
    if (roleName == null) {
      return ResponseEntity.ok().body(List.of());
    } else if (roleName.equals("Master Admin")) {
      List<AdminUser> adminUsers = adminUserService.listAdminUsers();
      List<AdminUserResponse> adminUserResponses = adminUsers.stream().map(adminUserMapper::toResponse).toList();
      return ResponseEntity.ok(adminUserResponses);
    } else {
      AdminUser adminUser = adminUserService.listAdminUser(userId);
      AdminUserResponse adminUserResponse = adminUserMapper.toResponse(adminUser);
      return ResponseEntity.ok(adminUserResponse);
    }
  }

  @GetMapping("/legacy/{id}")
  public ResponseEntity<AdminUserResponse> getAdminUser(@PathVariable @Valid Long id) {
    AdminUser adminUser = adminUserService.listAdminUser(id);
    AdminUserResponse adminUserResponse = adminUserMapper.toResponse(adminUser);
    return ResponseEntity.ok(adminUserResponse);
  }

  @PostMapping("/legacy")
  public ResponseEntity<ApiResponse> addAdminUser(@RequestBody @Valid AdminUserRequest adminUserRequest) {
    long startTime = System.currentTimeMillis();
    try {
      AdminUser adminUser = adminUserMapper.toEntity(adminUserRequest);
      adminUserService.addAdminUser(adminUser);
      long responseTime = System.currentTimeMillis() - startTime;
      ApiResponse response = new ApiResponse(responseTime, "success", 200, "Admin user added successfully",
          "Admin user added successfully", null);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      long responseTime = System.currentTimeMillis() - startTime;
      logger.error("Error adding admin user", e);
      ApiResponse response = new ApiResponse(responseTime, "error", 500, "Failed to add admin user",
          "Failed to add admin user: " + e.getMessage(), null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  @PostMapping("/change-password")
  public ResponseEntity<ApiResponse> changeAdminUserPassword(@RequestParam("username") @Valid String username,
      @RequestParam("oldPassword") @Valid String oldPassword,
      @RequestParam("newPassword") @Valid String newPassword) throws IOException {
    long startTime = System.currentTimeMillis();
    boolean isValidUser = adminUserLoginService.isValidAdminUser(username.toLowerCase(), oldPassword);
    if (!isValidUser) {
      long responseTime = System.currentTimeMillis() - startTime;
      return new ResponseEntity<>(new ApiResponse(responseTime, "Change Admin User Password",
          HttpStatus.FORBIDDEN.value(), "Failure", "Old Password does not match.", null), HttpStatus.FORBIDDEN);
    }

    try {
      byte[] byteSalt = securityService.getSalt();
      byte[] biteDigestPsw = securityService.getSaltedHashSHA512(newPassword, byteSalt);
      String strDigestPsw = securityService.toHex(biteDigestPsw);
      String strSalt = securityService.toHex(byteSalt);
      adminUserService.changeAdminUserPassword(strDigestPsw, strSalt, username.toLowerCase());

      long responseTime = System.currentTimeMillis() - startTime;
      return new ResponseEntity<>(new ApiResponse(responseTime, "Change Admin User Password",
          HttpStatus.OK.value(), "Success", "Password has been updated successfully.", null), HttpStatus.OK);
    } catch (NoSuchAlgorithmException ex) {
      logger.error("Salt generation error", ex);
      long responseTime = System.currentTimeMillis() - startTime;
      return new ResponseEntity<>(new ApiResponse(responseTime, "Change Admin User Password",
          HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", "Failed to update password: " + ex.getMessage(), null),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
