/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.xplaza.model.OrderDetails;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
  @Query(value = "select od.*, null as invoice_number from order_details od " +
      "where od.order_id = ?1", nativeQuery = true)
  OrderDetails findOrderDetails(Long id);
}
