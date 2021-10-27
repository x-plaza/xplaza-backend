package com.backend.xplaza.repository;

import com.backend.xplaza.model.OrderList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderListRepository extends JpaRepository<OrderList, Long> {
    @Query(value = "select o.order_id, o.total_price, o.discount_amount, o.net_total, o.grand_total_price, o.delivery_address, o.customer_id, " +
            "o.date_to_deliver, o.customer_name, " +
            "o.mobile_no, o.received_time, o.shop_id, o.shop_name, o.fk_status_id, st.status_name, " +
            "concat(o.delivery_schedule_start, '-' , o.delivery_schedule_end) as allotted_time, " +
            "o.fk_currency_id, cur.currency_name, cur.currency_sign " +
            "from orders o " +
            "left join currencies cur on cur.currency_id = o.fk_currency_id " +
            "left join status_catalogues st on o.fk_status_id = st.status_id " +
            "order by order_id desc", nativeQuery = true)
    List<OrderList> findAllOrdersByAdmin();

    @Query(value = "select o.order_id, o.total_price, o.discount_amount, o.net_total, o.grand_total_price, o.delivery_address, o.customer_id, " +
            "o.date_to_deliver, o.customer_name, " +
            "o.mobile_no, o.received_time, o.shop_id, o.shop_name, o.fk_status_id, st.status_name, " +
            "concat(o.delivery_schedule_start, '-' , o.delivery_schedule_end) as allotted_time, " +
            "o.fk_currency_id, cur.currency_name, cur.currency_sign " +
            "from orders o " +
            "left join currencies cur on cur.currency_id = o.fk_currency_id " +
            "left join status_catalogues st on o.fk_status_id = st.status_id " +
            "where st.status_name= ?1 order by order_id desc", nativeQuery = true)
    List<OrderList> findAllOrdersByStatusByAdmin(String status);

    @Query(value = "select o.order_id, o.total_price, o.discount_amount, o.net_total, o.grand_total_price, o.delivery_address, o.customer_id, " +
            "o.date_to_deliver, o.customer_name, " +
            "o.mobile_no, o.received_time, o.shop_id, o.shop_name, o.fk_status_id, st.status_name, " +
            "concat(o.delivery_schedule_start, '-' , o.delivery_schedule_end) as allotted_time, " +
            "o.fk_currency_id, cur.currency_name, cur.currency_sign " +
            "from orders o " +
            "left join currencies cur on cur.currency_id = o.fk_currency_id " +
            "left join status_catalogues st on o.fk_status_id = st.status_id " +
            "where st.status_name= ?1 and date(o.received_time) = ?2 order by order_id desc", nativeQuery = true)
    List<OrderList> findAllOrdersByFilterByAdmin(String status, Date order_date);

    @Query(value = "select o.order_id, o.total_price, o.discount_amount, o.net_total, o.grand_total_price, o.delivery_address, o.customer_id, " +
            "o.date_to_deliver, o.customer_name, " +
            "o.mobile_no, o.received_time, o.shop_id, o.shop_name, o.fk_status_id, st.status_name, " +
            "concat(o.delivery_schedule_start, '-' , o.delivery_schedule_end) as allotted_time, " +
            "o.fk_currency_id, cur.currency_name, cur.currency_sign " +
            "from orders o " +
            "left join currencies cur on cur.currency_id = o.fk_currency_id " +
            "left join status_catalogues st on o.fk_status_id = st.status_id " +
            "left join admin_user_shop_link ausl on ausl.shop_id = o.fk_shop_id " +
            "where ausl.admin_user_id = ?1 order by order_id desc", nativeQuery = true)
    List<OrderList> findAllOrdersAdminUserID(Long user_id);

    @Query(value = "select o.order_id, o.total_price, o.discount_amount, o.net_total, o.grand_total_price, o.delivery_address, o.customer_id, " +
            "o.date_to_deliver, o.customer_name, " +
            "o.mobile_no, o.received_time, o.shop_id, o.shop_name, o.fk_status_id, st.status_name, " +
            "concat(o.delivery_schedule_start, '-' , o.delivery_schedule_end) as allotted_time, " +
            "o.fk_currency_id, cur.currency_name, cur.currency_sign " +
            "from orders o " +
            "left join currencies cur on cur.currency_id = o.fk_currency_id " +
            "left join status_catalogues st on o.fk_status_id = st.status_id " +
            "left join admin_user_shop_link ausl on ausl.shop_id = o.fk_shop_id " +
            "where st.status_name= ?1 and ausl.admin_user_id = ?2 order by order_id desc", nativeQuery = true)
    List<OrderList> findAllOrdersByStatusAndAdminUserID(String status, Long user_id);

    @Query(value = "select o.order_id, o.total_price, o.discount_amount, o.net_total, o.grand_total_price, o.delivery_address, o.customer_id, " +
            "o.date_to_deliver, o.customer_name, " +
            "o.mobile_no, o.received_time, o.shop_id, o.shop_name, o.fk_status_id, st.status_name, " +
            "concat(o.delivery_schedule_start, '-' , o.delivery_schedule_end) as allotted_time, " +
            "o.fk_currency_id, cur.currency_name, cur.currency_sign " +
            "from orders o " +
            "left join currencies cur on cur.currency_id = o.fk_currency_id " +
            "left join status_catalogues st on o.fk_status_id = st.status_id " +
            "left join admin_user_shop_link ausl on ausl.shop_id = o.fk_shop_id " +
            "where st.status_name= ?1 and  date(o.received_time) = ?2 and ausl.admin_user_id = ?3 order by order_id desc", nativeQuery = true)
    List<OrderList> findAllOrdersByFilterAndAdminUserID(String status, Date order_date, Long user_id);
}
