/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.xplaza.model.Customer;
import com.backend.xplaza.model.CustomerDetails;
import com.backend.xplaza.repository.CustomerRepository;
import com.backend.xplaza.repository.CustomerUserRepository;

@Service
public class CustomerUserService {
  @Autowired
  private CustomerUserRepository customerUserRepo;
  @Autowired
  private CustomerRepository customerRepo;
  @Autowired
  private EmailSenderService emailSenderService;

  public void updateCustomer(Customer customer) {
    customerRepo.save(customer);
  }

  public String getCustomerNameByID(Long id) {
    return customerUserRepo.getUsername(id);
  }

  public void deleteCustomer(Long id) {
    customerUserRepo.deleteById(id);
  }

  public List<CustomerDetails> listCustomers() {
    return customerUserRepo.findAll();
  }

  public CustomerDetails listCustomer(String username) {
    return customerUserRepo.findCustomerByUsername(username);
  }

  public void changeCustomerPassword(String new_password, String salt, String user_name) {
    customerUserRepo.changePassword(new_password, salt, user_name);
  }

  public Optional<Customer> getCustomer(Long id) {
    return customerRepo.findById(id);
  }
}
