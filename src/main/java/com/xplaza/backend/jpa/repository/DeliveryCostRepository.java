/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.model.DeliveryCost;

public interface DeliveryCostRepository extends JpaRepository<DeliveryCost, Long> {
  @Query(value = "select concat(delivery_slab_start_range,'-',delivery_slab_end_range) as delivery_slab_range " +
      "from delivery_costs dc " +
      "where dc.delivery_cost_id = ?1", nativeQuery = true)
  String getName(Long id);
}
