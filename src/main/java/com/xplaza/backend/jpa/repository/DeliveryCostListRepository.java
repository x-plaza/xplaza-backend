/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.model.DeliveryCostList;

public interface DeliveryCostListRepository extends JpaRepository<DeliveryCostList, Long> {
  @Query(value = "select dc.delivery_cost_id, concat(delivery_slab_start_range,'-',delivery_slab_end_range) as delivery_slab_range, "
      +
      "dc.delivery_cost, dc.fk_currency_id, cur.currency_name, cur.currency_sign " +
      "from delivery_costs dc " +
      "left join currencies cur on dc.fk_currency_id = cur.currency_id " +
      "where dc.delivery_cost_id = ?1", nativeQuery = true)
  DeliveryCostList findDeliveryCostById(Long id);

  @Query(value = "select dc.delivery_cost_id, concat(delivery_slab_start_range,'-',delivery_slab_end_range) as delivery_slab_range, "
      +
      "dc.delivery_cost, dc.fk_currency_id, cur.currency_name, cur.currency_sign " +
      "from delivery_costs dc " +
      "left join currencies cur on dc.fk_currency_id = cur.currency_id ", nativeQuery = true)
  List<DeliveryCostList> findAllDeliveryCost();
}
