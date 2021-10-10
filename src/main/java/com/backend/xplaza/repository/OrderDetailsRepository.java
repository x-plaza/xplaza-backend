package com.backend.xplaza.repository;

import com.backend.xplaza.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    @Query(value = "select od.* from order_details od " +
            "where od.order_id = ?1", nativeQuery = true)
    OrderDetails findOrderDetailsByIdByAdmin(Long id);
}
