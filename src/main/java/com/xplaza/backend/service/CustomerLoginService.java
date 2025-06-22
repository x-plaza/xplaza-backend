/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.repository.CustomerRepository;
import com.xplaza.backend.mapper.CustomerMapper;
import com.xplaza.backend.service.entity.Customer;

@Service
public class CustomerLoginService {
  private final CustomerUserService customerUserService;
  private final SecurityService securityService;
  private final CustomerRepository customerRepo;
  private final CustomerMapper customerMapper;

  @Autowired
  public CustomerLoginService(CustomerUserService customerUserService, SecurityService securityService,
      CustomerRepository customerRepo, CustomerMapper customerMapper) {
    this.customerUserService = customerUserService;
    this.securityService = securityService;
    this.customerRepo = customerRepo;
    this.customerMapper = customerMapper;
  }

  public boolean isValidCustomerUser(String username, String password) {
    Customer customer = customerUserService.listCustomer(username);
    if (customer == null)
      return false;
    String strOrgSalt = customer.getSalt();
    byte[] byteSalt = securityService.fromHex(strOrgSalt);
    byte[] loginPassword = securityService.getSaltedHashSHA512(password, byteSalt);
    byte[] storedPassword = securityService.fromHex(customer.getPassword());
    return Arrays.equals(loginPassword, storedPassword);
  }

  public Customer getCustomerLoginDetails(String username) {
    return customerMapper.toEntityFromDao(customerRepo.findCustomerByUsername(username));
  }
}
