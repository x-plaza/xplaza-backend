/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.repository.CustomerRepository;
import com.xplaza.backend.jpa.repository.CustomerUserDAORepository;
import com.xplaza.backend.jpa.repository.CustomerUserRepository;
import com.xplaza.backend.mapper.CustomerUserMapper;
import com.xplaza.backend.model.Customer;
import com.xplaza.backend.model.CustomerDetails;
import com.xplaza.backend.service.entity.User;

@Service
public class CustomerUserService {
  @Autowired
  private CustomerUserRepository customerUserRepo;
  @Autowired
  private CustomerRepository customerRepo;
  @Autowired
  private CustomerUserDAORepository customerUserDAORepo;
  @Autowired
  private CustomerUserMapper customerUserMapper;

  public CustomerUserEntity getCustomerEntity(Long id) {
    User dao = customerUserDAORepo.findById(id).orElse(null);
    return customerUserMapper.toEntityFromDAO(dao);
  }

  public void updateCustomer(CustomerUserEntity entity) {
    User dao = customerUserMapper.toDAO(entity);
    customerUserDAORepo.save(dao);
  }

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
