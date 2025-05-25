/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.xplaza.model.OrderPlaceList;

public interface OrderPlaceListRepository extends JpaRepository<OrderPlaceList, Long> {
  @Query(value = "select o.order_id, o.shop_id, o.shop_name, null as invoice_number," +
      "o.customer_id, o.customer_name, o.mobile_no, o.delivery_address, o.additional_info, o.remarks," +
      "concat(o.delivery_schedule_start, '-' , o.delivery_schedule_end) as allotted_time," +
      "o.received_time, o.date_to_deliver, o.fk_payment_type_id, pt.payment_type_name," +
      "o.fk_status_id, st.status_name," +
      "o.fk_currency_id, cur.currency_name, cur.currency_sign," +
      "o.total_price, o.discount_amount, o.net_total, o.coupon_id, o.coupon_code, o.coupon_amount, o.delivery_cost_id, o.delivery_cost, o.grand_total_price "
      +
      "from orders o " +
      "left join payment_types pt on pt.payment_type_id = o.fk_payment_type_id " +
      "left join currencies cur on cur.currency_id = o.fk_currency_id " +
      "left join status_catalogues st on o.fk_status_id = st.status_id " +
      "where o.customer_id = ?1 " +
      "order by o.order_id desc", nativeQuery = true)
  List<OrderPlaceList> findAllOrdersByCustomer(Long customer_id);

  @Query(value = "select o.order_id, o.shop_id, o.shop_name,null as invoice_number," +
      "o.customer_id, o.customer_name, o.mobile_no, o.delivery_address, o.additional_info, o.remarks," +
      "concat(o.delivery_schedule_start, '-' , o.delivery_schedule_end) as allotted_time," +
      "o.received_time, o.date_to_deliver, o.fk_payment_type_id, pt.payment_type_name," +
      "o.fk_status_id, st.status_name," +
      "o.fk_currency_id, cur.currency_name, cur.currency_sign," +
      "o.total_price, o.discount_amount, o.net_total, o.coupon_id, o.coupon_code, o.coupon_amount, o.delivery_cost_id, o.delivery_cost, o.grand_total_price "
      +
      "from orders o " +
      "left join payment_types pt on pt.payment_type_id = o.fk_payment_type_id " +
      "left join currencies cur on cur.currency_id = o.fk_currency_id " +
      "left join status_catalogues st on o.fk_status_id = st.status_id " +
      "where o.customer_id = ?1 and st.status_name= ?2 " +
      "order by o.order_id desc", nativeQuery = true)
  List<OrderPlaceList> findAllOrdersByStatusByCustomer(Long customer_id, String status);

  @Query(value = "select o.order_id, o.shop_id, o.shop_name,null as invoice_number," +
      "o.customer_id, o.customer_name, o.mobile_no, o.delivery_address, o.additional_info, o.remarks," +
      "concat(o.delivery_schedule_start, '-' , o.delivery_schedule_end) as allotted_time," +
      "o.received_time, o.date_to_deliver, o.fk_payment_type_id, pt.payment_type_name," +
      "o.fk_status_id, st.status_name," +
      "o.fk_currency_id, cur.currency_name, cur.currency_sign," +
      "o.total_price, o.discount_amount, o.net_total, o.coupon_id, o.coupon_code, o.coupon_amount, o.delivery_cost_id, o.delivery_cost, o.grand_total_price "
      +
      "from orders o " +
      "left join payment_types pt on pt.payment_type_id = o.fk_payment_type_id " +
      "left join currencies cur on cur.currency_id = o.fk_currency_id " +
      "left join status_catalogues st on o.fk_status_id = st.status_id " +
      "where o.customer_id = ?1 and st.status_name= ?2 and o.date_to_deliver = ?3 " +
      "order by o.order_id desc", nativeQuery = true)
  List<OrderPlaceList> findAllOrdersByFilterByCustomer(Long customer_id, String status, Date order_date);
}
