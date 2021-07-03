package com.backend.xplaza.repository;

import com.backend.xplaza.model.OrderList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderListRepository extends JpaRepository<OrderList, Long> {
    @Query(value = "select o.order_id, o.total_price, o.discount_amount, o.grand_total_price, o.delivery_address, o.fk_customer_id, " +
            "o.date_to_deliver, concat(c.first_name,' ',c.last_name) as customer_name, " +
            "c.mobile_no, o.received_time, o.fk_shop_id, s.shop_name, o.fk_status_id, st.status_name, " +
            "concat(d.delivery_schedule_start, '-' , d.delivery_schedule_end) as allotted_time " +
            "from orders o " +
            "left join shops s on o.fk_shop_id = s.shop_id " +
            "left join customers c on o.fk_customer_id = c.customer_id " +
            "left join delivery_schedules d on o.fk_delivery_schedule_id = d.delivery_schedule_id " +
            "left join status_catalogues st on o.fk_status_id = st.status_id order by order_id desc", nativeQuery = true)
    List<OrderList> findAllOrders();


    @Query(value = "select o.order_id, o.total_price, o.discount_amount, o.grand_total_price, o.delivery_address, o.fk_customer_id, " +
            "o.date_to_deliver, concat(c.first_name,' ',c.last_name) as customer_name, " +
            "c.mobile_no, o.received_time, o.fk_shop_id, s.shop_name, o.fk_status_id, st.status_name, " +
            "concat(d.delivery_schedule_start, '-' , d.delivery_schedule_end) as allotted_time " +
            "from orders o " +
            "left join shops s on o.fk_shop_id = s.shop_id " +
            "left join customers c on o.fk_customer_id = c.customer_id " +
            "left join delivery_schedules d on o.fk_delivery_schedule_id = d.delivery_schedule_id " +
            "left join status_catalogues st on o.fk_status_id = st.status_id " +
            "where st.status_name= ?1 order by order_id desc", nativeQuery = true)
    List<OrderList> findAllOrdersByStatus(String status);


    @Query(value = "select o.order_id, o.total_price, o.discount_amount, o.grand_total_price, o.delivery_address, o.fk_customer_id, " +
            "o.date_to_deliver, concat(c.first_name,' ',c.last_name) as customer_name, " +
            "c.mobile_no, o.received_time, o.fk_shop_id, s.shop_name, o.fk_status_id, st.status_name, " +
            "concat(d.delivery_schedule_start, '-' , d.delivery_schedule_end) as allotted_time " +
            "from orders o " +
            "left join shops s on o.fk_shop_id = s.shop_id " +
            "left join customers c on o.fk_customer_id = c.customer_id " +
            "left join delivery_schedules d on o.fk_delivery_schedule_id = d.delivery_schedule_id " +
            "left join status_catalogues st on o.fk_status_id = st.status_id " +
            "where st.status_name= ?1 and o.date_to_deliver = ?2 order by order_id desc", nativeQuery = true)
    List<OrderList> findAllOrdersByFilter(String status, Date order_date);


    @Query(value = "select o.order_id, o.total_price, o.discount_amount, o.grand_total_price, o.delivery_address, o.fk_customer_id, " +
            "o.date_to_deliver, concat(c.first_name,' ',c.last_name) as customer_name, " +
            "c.mobile_no, o.received_time, o.fk_shop_id, s.shop_name, o.fk_status_id, st.status_name, " +
            "concat(d.delivery_schedule_start, '-' , d.delivery_schedule_end) as allotted_time " +
            "from orders o " +
            "left join shops s on o.fk_shop_id = s.shop_id " +
            "left join customers c on o.fk_customer_id = c.customer_id " +
            "left join delivery_schedules d on o.fk_delivery_schedule_id = d.delivery_schedule_id " +
            "left join status_catalogues st on o.fk_status_id = st.status_id " +
            "left join admin_user_shop_link ausl on ausl.shop_id = o.fk_shop_id " +
            "where ausl.admin_user_id = ?1 order by order_id desc", nativeQuery = true)
    List<OrderList> findAllOrdersUserID(long user_id);


    @Query(value = "select o.order_id, o.total_price, o.discount_amount, o.grand_total_price, o.delivery_address, o.fk_customer_id, " +
            "o.date_to_deliver, concat(c.first_name,' ',c.last_name) as customer_name, " +
            "c.mobile_no, o.received_time, o.fk_shop_id, s.shop_name, o.fk_status_id, st.status_name, " +
            "concat(d.delivery_schedule_start, '-' , d.delivery_schedule_end) as allotted_time " +
            "from orders o " +
            "left join shops s on o.fk_shop_id = s.shop_id " +
            "left join customers c on o.fk_customer_id = c.customer_id " +
            "left join delivery_schedules d on o.fk_delivery_schedule_id = d.delivery_schedule_id " +
            "left join status_catalogues st on o.fk_status_id = st.status_id " +
            "left join admin_user_shop_link ausl on ausl.shop_id = o.fk_shop_id " +
            "where st.status_name= ?1 and ausl.admin_user_id = ?2 order by order_id desc", nativeQuery = true)
    List<OrderList> findAllOrdersByStatusAndUserID(String status, long user_id);


    @Query(value = "select o.order_id, o.total_price, o.discount_amount, o.grand_total_price, o.delivery_address, o.fk_customer_id, " +
            "o.date_to_deliver, concat(c.first_name,' ',c.last_name) as customer_name, " +
            "c.mobile_no, o.received_time, o.fk_shop_id, s.shop_name, o.fk_status_id, st.status_name, " +
            "concat(d.delivery_schedule_start, '-' , d.delivery_schedule_end) as allotted_time " +
            "from orders o " +
            "left join shops s on o.fk_shop_id = s.shop_id " +
            "left join customers c on o.fk_customer_id = c.customer_id " +
            "left join delivery_schedules d on o.fk_delivery_schedule_id = d.delivery_schedule_id " +
            "left join status_catalogues st on o.fk_status_id = st.status_id " +
            "left join admin_user_shop_link ausl on ausl.shop_id = o.fk_shop_id " +
            "where st.status_name= ?1 and o.date_to_deliver = ?2 and ausl.admin_user_id = ?3 order by order_id desc", nativeQuery = true)
    List<OrderList> findAllOrdersByFilterAndUserID(String status, Date order_date, long user_id);
}
