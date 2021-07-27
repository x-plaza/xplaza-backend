package com.backend.xplaza.repository;

import com.backend.xplaza.model.DeliveryCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryCostRepository extends JpaRepository<DeliveryCost, Long> {
    @Query(value = "select concat(delivery_slab_start_range,'-',delivery_slab_end_range) as delivery_slab_range " +
            "from delivery_costs dc " +
            "where dc.delivery_cost_id = ?1", nativeQuery = true)
    String getName(Long id);
}
