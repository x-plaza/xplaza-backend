/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.model.AdminLogin;
import com.xplaza.backend.model.AdminUser;
import com.xplaza.backend.model.ConfirmationToken;
import com.xplaza.backend.service.AdminUserService;
import com.xplaza.backend.service.ConfirmationTokenService;
import com.xplaza.backend.service.LoginService;
import com.xplaza.backend.service.SecurityService;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {
  @Autowired
  private LoginService loginService;

  @Autowired
  private AdminUserService adminUserService;

  @Autowired
  private ConfirmationTokenService confirmationTokenService;

  @Autowired
  private SecurityService securityService;

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

  @PostMapping
  public ResponseEntity<String> loginAdminUser(@RequestParam("username") @Valid String username,
      @RequestParam("password") @Valid String password) throws IOException {
    start = new Date();
    AdminLogin dtos = loginService.getAdminUserDetails(username.toLowerCase());
    if (dtos != null) {
      Boolean isValidUser;
      if (username.equalsIgnoreCase("admin@gmail.com"))
        isValidUser = loginService.isVaidMasterAdmin(username.toLowerCase(), password);
      else
        isValidUser = loginService.isVaidUser(username.toLowerCase(), password);

      if (isValidUser) {
        dtos.setAuthentication(true);
      } else {
        dtos.setAuthData(null);
        dtos.setShopList(null);
        dtos.setPermissions(null);
      }
    } else {
      dtos = new AdminLogin();
      dtos.setAuthentication(false);
      dtos.setAuthData(null);
      dtos.setShopList(null);
      dtos.setPermissions(null);
    }
    end = new Date();
    responseTime = end.getTime() - start.getTime();

    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Authentication And ACL\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" +
        mapper.writeValueAsString(dtos) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/send-otp")
  public ResponseEntity<ApiResponse> sendOTP(@RequestParam("username") @Valid String username) {
    start = new Date();
    AdminUser user = adminUserService.listAdminUser(username.toLowerCase());
    if (user == null) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Send OTP", HttpStatus.FORBIDDEN.value(),
          "Failed", "User Does Not Exist!", null), HttpStatus.FORBIDDEN);
    }
    confirmationTokenService.sendOTP(username.toLowerCase());
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Send OTP", HttpStatus.CREATED.value(),
        "Success", "An OTP has been sent to the email.", null), HttpStatus.CREATED);
  }

  @PostMapping("/validate-otp")
  public ResponseEntity<ApiResponse> validateOTP(@RequestParam("username") @Valid String username,
      @RequestParam("OTP") @Valid String OTP) {
    start = new Date();
    ConfirmationToken token = confirmationTokenService.getConfirmationToken(OTP);
    if (token == null) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Validate OTP", HttpStatus.FORBIDDEN.value(),
          "Failed", "OTP does not match!", null), HttpStatus.FORBIDDEN);
    }
    if (!token.getEmail().equals(username.toLowerCase())) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Validate OTP", HttpStatus.FORBIDDEN.value(),
          "Failed", "OTP does not match!", null), HttpStatus.FORBIDDEN);
    }
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Validate OTP", HttpStatus.OK.value(),
        "Success", "OTP matched.", null), HttpStatus.OK);
  }

  @PostMapping("/change-password")
  public ResponseEntity<ApiResponse> changeAdminUserPassword(@RequestParam("username") @Valid String username,
      @RequestParam("newPassword") @Valid String newPassword) {
    start = new Date();
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
    return new ResponseEntity<>(new ApiResponse(responseTime, "Change Admin User Password", HttpStatus.OK.value(),
        "Success", "Password has been updated successfully.", null), HttpStatus.OK);
  }
}
