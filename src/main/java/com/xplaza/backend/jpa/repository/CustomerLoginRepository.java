/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.CustomerDao;

public interface CustomerLoginRepository extends JpaRepository<CustomerDao, Long> {
  @Query(value = "select customer_id, concat(first_name,' ', last_name) as customer_name, email, false as authentication  "
      +
      "from customers where email = ?1", nativeQuery = true)
  CustomerDao findCustomerByUsername(String username);
}
