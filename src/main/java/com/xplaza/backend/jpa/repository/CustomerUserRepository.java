/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.jpa.dao.CustomerDao;

public interface CustomerUserRepository extends JpaRepository<CustomerDao, Long> {
  @Query(value = "select email from customers where customer_id = ?1", nativeQuery = true)
  String getUsername(Long id);

  @Query(value = "select * from customers where email = ?1", nativeQuery = true)
  CustomerDao findCustomerByUsername(String username);

  @Modifying
  @Transactional
  @Query(value = "update customers set password=?1, salt=?2 where email=?3", nativeQuery = true)
  void changePassword(String newPassword, String salt, String email);
}
