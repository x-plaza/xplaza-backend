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

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.model.ConfirmationToken;
import com.xplaza.backend.model.CustomerLogin;
import com.xplaza.backend.service.ConfirmationTokenService;
import com.xplaza.backend.service.CustomerLoginService;
import com.xplaza.backend.service.CustomerUserService;
import com.xplaza.backend.service.SecurityService;

@RestController
@RequestMapping("/api/v1/customer-login")
public class CustomerLoginController extends BaseController {
  @Autowired
  private CustomerLoginService customerLoginService;

  @Autowired
  private CustomerUserService customerUserService;

  @Autowired
  private ConfirmationTokenService confirmationTokenService;

  @Autowired
  private SecurityService securityService;

  private Date start, end;
  private Long responseTime;

  @PostMapping
  public ResponseEntity<String> loginCustomerUser(@RequestParam("username") @Valid String username,
      @RequestParam("password") @Valid String password) throws IOException {
    start = new Date();
    Boolean isValidUser = customerLoginService.isVaidUser(username.toLowerCase(), password);
    CustomerLogin dtos = new CustomerLogin();
    if (isValidUser) {
      dtos = customerLoginService.getCustomerLoginDetails(username.toLowerCase());
      dtos.setAuthentication(true);
    } else {
      dtos.setAuthentication(false);
    }
    end = new Date();
    responseTime = end.getTime() - start.getTime();

    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Customer Authentication\",\n" +
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
    CustomerLogin customer = customerLoginService.getCustomerLoginDetails(username.toLowerCase());
    if (customer == null) {
      end = new Date();
      responseTime = end.getTime() - start.getTime();
      return new ResponseEntity<>(new ApiResponse(responseTime, "Send OTP", HttpStatus.FORBIDDEN.value(),
          "Failed", "User Does Not Exist!", null), HttpStatus.FORBIDDEN);
    }
    confirmationTokenService.sendOTPToCustomer(username.toLowerCase());
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Send OTP", HttpStatus.CREATED.value(),
        "Success", "An OTP has been sent to your email.", null), HttpStatus.CREATED);
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
        "Success", "OTP matched!", null), HttpStatus.OK);
  }

  @PostMapping("/change-password")
  public ResponseEntity<ApiResponse> changeUserPassword(@RequestParam("username") @Valid String username,
      @RequestParam("newPassword") @Valid String newPassword)
      throws IOException {
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
    customerUserService.changeCustomerPassword(strDigestPsw, strSalt, username.toLowerCase());
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Change Customer User Password",
        HttpStatus.OK.value(), "Success", "Password has been updated successfully.", null), HttpStatus.OK);
  }

}
