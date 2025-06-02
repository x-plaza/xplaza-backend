/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
  @Modifying
  @Transactional
  @Query(value = "update orders set fk_status_id = ?3, remarks=?2 where order_id = ?1", nativeQuery = true)
  void updateOrderStatus(Long order_id, String remarks, Long status);

  @Query(value = "select o.* from orders o where o.order_id = ?1", nativeQuery = true)
  Order findOrderById(Long order_id);
}
