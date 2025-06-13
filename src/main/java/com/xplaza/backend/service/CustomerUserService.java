/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.dao.CustomerDao;
import com.xplaza.backend.jpa.repository.CustomerRepository;
import com.xplaza.backend.jpa.repository.CustomerUserRepository;
import com.xplaza.backend.mapper.CustomerMapper;
import com.xplaza.backend.service.entity.Customer;

@Service
public class CustomerUserService {
  @Autowired
  private CustomerUserRepository customerUserRepo;
  @Autowired
  private CustomerRepository customerRepo;
  @Autowired
  private CustomerMapper customerMapper;

  public void updateCustomer(Customer entity) {
    CustomerDao dao = customerMapper.toDao(entity);
    customerRepo.save(dao);
  }

  public void deleteCustomer(Long id) {
    customerUserRepo.deleteById(id);
  }

  public List<Customer> listCustomers() {
    return customerUserRepo.findAll().stream().map(customerMapper::toEntityFromDao).toList();
  }

  public Customer listCustomer(String username) {
    return customerMapper.toEntityFromDao(customerUserRepo.findCustomerByUsername(username));
  }

  public void changeCustomerPassword(String new_password, String salt, String user_name) {
    customerUserRepo.changePassword(new_password, salt, user_name);
  }

  public Customer getCustomer(Long id) {
    return customerMapper.toEntityFromDao(customerRepo.findById(id).orElseThrow(
        () -> new RuntimeException("Customer not found with id: " + id)));
  }
}
