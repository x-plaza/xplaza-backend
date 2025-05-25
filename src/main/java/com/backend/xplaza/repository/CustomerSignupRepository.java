/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.backend.xplaza.model.CustomerDetails;

public interface CustomerSignupRepository extends JpaRepository<CustomerDetails, Long> {

  @Modifying
  @Transactional
  @Query(value = "update customers set password=?1, salt=?2 where email=?3", nativeQuery = true)
  void changePassword(String new_password, String salt, String user_name);
}
