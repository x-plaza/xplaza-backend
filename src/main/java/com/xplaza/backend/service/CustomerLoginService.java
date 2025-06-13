/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.repository.CustomerRepository;
import com.xplaza.backend.mapper.CustomerMapper;
import com.xplaza.backend.service.entity.Customer;

@Service
public class CustomerLoginService {
  @Autowired
  private CustomerUserService customerUserService;
  @Autowired
  private SecurityService securityService;
  @Autowired
  private CustomerRepository customerRepo;
  @Autowired
  private CustomerMapper customerMapper;

  public Boolean isVaidUser(String username, String password) throws IOException {
    Customer customer = customerUserService.listCustomer(username);
    if (customer == null)
      return false;
    String strOrgSalt = customer.getSalt();
    byte[] byteSalt = securityService.fromHex(strOrgSalt);
    byte[] loginPassword = securityService.getSaltedHashSHA512(password, byteSalt);
    byte[] storedPassword = securityService.fromHex(customer.getPassword());
    boolean result = Arrays.equals(loginPassword, storedPassword);
    return result;
  }

  public Customer getCustomerLoginDetails(String username) {
    return customerMapper.toEntityFromDao(customerRepo.findCustomerByUsername(username));
  }
}
