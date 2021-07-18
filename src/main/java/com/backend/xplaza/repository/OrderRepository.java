package com.backend.xplaza.repository;

import com.backend.xplaza.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Modifying
    @Transactional
    @Query(value = "update orders set fk_status_id = ?2 where order_id = ?1", nativeQuery = true)
    void updateOrderStatus(Long invoice_number, Long status);
}
