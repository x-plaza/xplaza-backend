/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import com.xplaza.backend.service.entity.ConfirmationToken;

@RestController
@RequestMapping("/api/v1/admin-users")
public class AdminUserController extends BaseController {
  @Autowired
  private AdminUserService adminUserService;

  @Autowired
  private SecurityService securityService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private AdminUserLoginService adminUserLoginService;

  @Autowired
  private ConfirmationTokenService confirmationTokenService;

  @Autowired
  private AdminUserMapper adminUserMapper;

  private Date start, end;
  private Long responseTime;

  @GetMapping
  public ResponseEntity<String> getAdminUsers(@RequestParam(value = "user_id") @Valid Long userId)
      throws JsonProcessingException {
    start = new Date();
    ObjectMapper mapper = new ObjectMapper();
    String data;
    String role_name = roleService.getRoleNameByUserID(userId);
    if (role_name == null)
      data = null;
    else if (role_name.equals("Master Admin")) {
      List<AdminUser> adminUsers = adminUserService.listAdminUsers();
      List<AdminUserResponse> adminUserResponses = adminUsers.stream().map(adminUserMapper::toResponse).toList();
      data = mapper.writeValueAsString(adminUserResponses);
    } else {
      AdminUser adminUser = adminUserService.listAdminUser(userId);
      AdminUserResponse adminUserResponse = adminUserMapper.toResponse(adminUser);
      data = mapper.writeValueAsString(adminUserResponse);
    }
    end = new Date();
    responseTime = end.getTime() - start.getTime();

    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Admin User List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + data + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getAdminUser(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    AdminUser adminUser = adminUserService.listAdminUser(id);
    AdminUserResponse adminUserResponse = adminUserMapper.toResponse(adminUser);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Admin User List\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(adminUserResponse) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ApiResponse> addAdminUser(@RequestBody @Valid AdminUserRequest adminUserRequest) {
    start = new Date();
    ConfirmationToken token = confirmationTokenService.getConfirmationToken(adminUserRequest.getConfirmationCode());
    if (token == null) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Add Admin User", HttpStatus.FORBIDDEN.value(),
          "Failed", "Confirmation code does not match!", null), HttpStatus.FORBIDDEN);
    }
    if (!token.getEmail().equals(adminUserRequest.getUserName().toLowerCase())) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Add Admin User", HttpStatus.FORBIDDEN.value(),
          "Failed", "Confirmation code does not match!", null), HttpStatus.FORBIDDEN);
    }
    Date today = new Date();
    if (today.after(token.getValidTill())) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Add Admin User", HttpStatus.FORBIDDEN.value(),
          "Failed", "Confirmation code expired! Please get a new code!", null), HttpStatus.FORBIDDEN);
    }
    AdminUser existingAdminUser = adminUserService.listAdminUser(adminUserRequest.getUserName().toLowerCase());
    if (existingAdminUser != null) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Add Admin User", HttpStatus.FORBIDDEN.value(),
          "Failed", "User Already Exist!", null), HttpStatus.FORBIDDEN);
    }
    // Encrypt Password with Salt
    String temp_password = adminUserRequest.getPassword();
    byte[] byteSalt = null;
    try {
      byteSalt = securityService.getSalt();
    } catch (NoSuchAlgorithmException ex) {
      Logger.getLogger("Salt error").log(Level.SEVERE, null, ex);
    }
    byte[] biteDigestPsw = securityService.getSaltedHashSHA512(adminUserRequest.getPassword(), byteSalt);
    String strDigestPsw = securityService.toHex(biteDigestPsw);
    String strSalt = securityService.toHex(byteSalt);
    adminUserRequest.setPassword(strDigestPsw);
    adminUserRequest.setSalt(strSalt);
    adminUserRequest.setUserName(adminUserRequest.getUserName().toLowerCase());
    AdminUser adminUser = adminUserMapper.toEntity(adminUserRequest);
    adminUserService.addAdminUser(adminUser);
    adminUserService.sendLoginDetails(adminUserRequest.getUserName(), temp_password);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Add Admin User", HttpStatus.CREATED.value(),
        "Success", "Admin User has been created successfully.", null), HttpStatus.CREATED);
  }

  @PostMapping("/change-password")
  public ResponseEntity<ApiResponse> changeAdminUserPassword(@RequestParam("username") @Valid String username,
      @RequestParam("oldPassword") @Valid String oldPassword,
      @RequestParam("newPassword") @Valid String newPassword) throws IOException {
    start = new Date();
    boolean isValidUser = adminUserLoginService.isValidAdminUser(username.toLowerCase(), oldPassword);
    if (!isValidUser) {
      return new ResponseEntity<>(new ApiResponse(responseTime, "Change Admin User Password",
          HttpStatus.FORBIDDEN.value(), "Failure", "Old Password does not match.", null), HttpStatus.FORBIDDEN);
    }
    byte[] byteSalt = null;
    try {
      byteSalt = securityService.getSalt();
    } catch (NoSuchAlgorithmException ex) {
      Logger.getLogger("Salt error").log(Level.SEVERE, null, ex);
    }
    byte[] biteDigestPsw = securityService.getSaltedHashSHA512(newPassword, byteSalt);
    String strDigestPsw = securityService.toHex(biteDigestPsw);
    String strSalt = securityService.toHex(byteSalt);
    adminUserService.changeAdminUserPassword(strDigestPsw, strSalt, username.toLowerCase());
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Change Admin User Password",
        HttpStatus.OK.value(), "Success", "Password has been updated successfully.", null), HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateAdminUser(@RequestBody @Valid AdminUserRequest adminUserRequest) {
    start = new Date();
    AdminUser adminUser = adminUserMapper.toEntity(adminUserRequest);
    adminUserService.updateAdminUser(adminUser);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Admin User", HttpStatus.OK.value(),
        "Success", "Admin User has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteAdminUser(@PathVariable @Valid Long id) {
    String admin_user_name = adminUserService.getAdminUserNameByID(id);
    start = new Date();
    adminUserService.deleteAdminUser(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Admin User", HttpStatus.OK.value(),
        "Success", admin_user_name + " has been deleted.", null), HttpStatus.OK);
  }
}
