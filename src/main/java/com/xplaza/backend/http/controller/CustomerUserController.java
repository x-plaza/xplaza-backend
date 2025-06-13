/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.http.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.http.dto.request.CustomerUserRequest;
import com.xplaza.backend.http.dto.response.CustomerUserResponse;
import com.xplaza.backend.mapper.CustomerMapper;
import com.xplaza.backend.service.CustomerLoginService;
import com.xplaza.backend.service.CustomerUserService;
import com.xplaza.backend.service.SecurityService;
import com.xplaza.backend.service.entity.Customer;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerUserController extends BaseController {
  @Autowired
  private CustomerUserService customerUserService;
  @Autowired
  private CustomerLoginService customerLoginService;
  @Autowired
  private SecurityService securityService;
  @Autowired
  private CustomerMapper customerMapper;

  @GetMapping("/{id}")
  public ResponseEntity<CustomerUserResponse> getCustomer(@PathVariable @Valid Long id) {
    Customer entity = customerUserService.getCustomer(id);
    CustomerUserResponse dto = customerMapper.toResponse(entity);
    return ResponseEntity.ok(dto);
  }

  @PutMapping
  public ResponseEntity<Void> updateCustomer(@RequestBody @Valid CustomerUserRequest request) {
    Customer entity = customerMapper.toEntity(request);
    customerUserService.updateCustomer(entity);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable @Valid Long id) {
    customerUserService.deleteCustomer(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/change-password")
  public ResponseEntity<Void> changeCustomerUserPassword(@RequestParam("username") @Valid String username,
      @RequestParam("oldPassword") @Valid String oldPassword,
      @RequestParam("newPassword") @Valid String newPassword) throws IOException {
    boolean isValidUser = customerLoginService.isVaidUser(username.toLowerCase(), oldPassword);
    if (!isValidUser) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    byte[] byteSalt = null;
    try {
      byteSalt = securityService.getSalt();
    } catch (NoSuchAlgorithmException ex) {
      Logger.getLogger("Salt error").log(Level.SEVERE, null, ex);
    }
    byte[] byteDigestPsw = securityService.getSaltedHashSHA512(newPassword, byteSalt);
    String strDigestPsw = securityService.toHex(byteDigestPsw);
    String strSalt = securityService.toHex(byteSalt);
    customerUserService.changeCustomerPassword(strDigestPsw, strSalt, username.toLowerCase());
    return ResponseEntity.ok().build();
  }
}
