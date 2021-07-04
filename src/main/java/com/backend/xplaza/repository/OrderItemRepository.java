package com.backend.xplaza.repository;

import com.backend.xplaza.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query(value = "select order_item_name from order_items where order_item_id = ?1", nativeQuery = true)
    String getName(long id);

    @Query(value = "select * from order_items where order_item_id = ?1", nativeQuery = true)
    OrderItem findOrderItemById(long id);
}
