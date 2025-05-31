/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Optional;
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
import com.xplaza.backend.model.Customer;
import com.xplaza.backend.service.CustomerLoginService;
import com.xplaza.backend.service.CustomerUserService;
import com.xplaza.backend.service.SecurityService;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerUserController extends BaseController {
  @Autowired
  private CustomerUserService customerUserService;

  @Autowired
  private CustomerLoginService customerLoginService;

  @Autowired
  private SecurityService securityService;

  private Date start, end;
  private Long responseTime;

  @GetMapping("/{id}")
  public ResponseEntity<String> getCustomer(@PathVariable @Valid Long id) throws JsonProcessingException {
    start = new Date();
    Optional<Customer> dtos = customerUserService.getCustomer(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    ObjectMapper mapper = new ObjectMapper();
    String response = "{\n" +
        "  \"responseTime\": " + responseTime + ",\n" +
        "  \"responseType\": \"Customer Details\",\n" +
        "  \"status\": 200,\n" +
        "  \"response\": \"Success\",\n" +
        "  \"msg\": \"\",\n" +
        "  \"data\":" + mapper.writeValueAsString(dtos.get()) + "\n}";
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<ApiResponse> updateCustomer(@RequestBody @Valid Customer customer) {
    start = new Date();
    customerUserService.updateCustomer(customer);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Update Customer", HttpStatus.OK.value(),
        "Success", "Customer has been updated.", null), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable @Valid Long id) {
    String customer_name = customerUserService.getCustomerNameByID(id);
    start = new Date();
    customerUserService.deleteCustomer(id);
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Delete Customer", HttpStatus.OK.value(),
        "Success", "User " + customer_name + " has been deleted.", null), HttpStatus.OK);
  }

  @PostMapping("/change-password")
  public ResponseEntity<ApiResponse> changeCustomerUserPassword(@RequestParam("username") @Valid String username,
      @RequestParam("oldPassword") @Valid String oldPassword,
      @RequestParam("newPassword") @Valid String newPassword) throws IOException {
    start = new Date();
    boolean isValidUser = customerLoginService.isVaidUser(username.toLowerCase(), oldPassword);
    if (!isValidUser) {
      return new ResponseEntity<>(new ApiResponse(responseTime, "Change Customer User Password",
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
    customerUserService.changeCustomerPassword(strDigestPsw, strSalt, username.toLowerCase());
    end = new Date();
    responseTime = end.getTime() - start.getTime();
    return new ResponseEntity<>(new ApiResponse(responseTime, "Change Customer User Password",
        HttpStatus.OK.value(), "Success", "Password has been updated successfully.", null), HttpStatus.OK);
  }
}
