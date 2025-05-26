/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.model.CustomerDetails;
import com.xplaza.backend.model.CustomerLogin;
import com.xplaza.backend.repository.CustomerLoginRepository;

@Service
public class CustomerLoginService {
  @Autowired
  private CustomerUserService customerUserService;
  @Autowired
  private SecurityService securityService;
  @Autowired
  private CustomerLoginRepository customerLoginRepo;

  public Boolean isVaidUser(String username, String password) throws IOException {
    CustomerDetails customer = customerUserService.listCustomer(username);
    if (customer == null)
      return false;
    String strOrgSalt = customer.getSalt();
    byte[] byteSalt = securityService.fromHex(strOrgSalt);
    byte[] loginPassword = securityService.getSaltedHashSHA512(password, byteSalt);
    byte[] storedPassword = securityService.fromHex(customer.getPassword());
    boolean result = Arrays.equals(loginPassword, storedPassword);
    return result;
  }

  public CustomerLogin getCustomerLoginDetails(String username) {
    return customerLoginRepo.findCustomerByUsername(username);
  }
}
