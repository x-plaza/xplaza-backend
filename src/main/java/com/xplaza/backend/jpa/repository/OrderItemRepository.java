/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
  @Query(value = "select order_item_name from order_items where order_item_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "select * from order_items where order_item_id = ?1", nativeQuery = true)
  OrderItem findOrderItemById(Long id);
}
