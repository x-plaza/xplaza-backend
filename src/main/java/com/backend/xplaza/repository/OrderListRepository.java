package com.backend.xplaza.repository;

import com.backend.xplaza.model.OrderList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderListRepository extends JpaRepository<OrderList, Long> {
    @Query(value = "select o.order_id, o.total_price, o.discount_amount, o.grand_total_price, o.delivery_address, concat(c.first_name,' ',c.last_name) as customer_name, " +
            "c.mobile_no, o.received_time, o.fk_shop_id, s.shop_name, o.fk_status_id, st.status_name, " +
            "concat(d.delivery_schedule_start, '-' , d.delivery_schedule_end) as allotted_time " +
            "from orders o " +
            "left join shops s on o.fk_shop_id = s.shop_id " +
            "left join customers c on o.fk_customer_id = c.customer_id " +
            "left join delivery_schedules d on o.fk_delivery_schedule_id = d.delivery_schedule_id " +
            "left join status_catalogues st on o.fk_status_id = st.status_id", nativeQuery = true)
    List<OrderList> findAllOrders();
}
