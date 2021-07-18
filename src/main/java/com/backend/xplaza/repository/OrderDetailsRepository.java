package com.backend.xplaza.repository;

import com.backend.xplaza.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    @Query(value = "select o.order_id, o.total_price, o.discount_amount, o.grand_total_price, o.delivery_address, o.fk_customer_id, o.fk_shop_id, o.fk_delivery_schedule_id, " +
            "o.fk_delivery_cost_id, o.fk_payment_type_id, o.fk_status_id, COALESCE(o.fk_coupon_id, 0) as fk_coupon_id, o.received_time, o.date_to_deliver, o.fk_currency_id, " +
            "concat(c.first_name,' ',c.last_name) as customer_name, c.mobile_no, " +
            "s.shop_name, " +
            "st.status_name, " +
            "concat(ds.delivery_schedule_start, '-' , ds.delivery_schedule_end) as allotted_time, dc.delivery_cost, " +
            "pt.payment_type_name, " +
            "d.delivery_id, d.person_name, d.contact_no, " +
            "COALESCE(cou.coupon_code,'N/A') as coupon_code, COALESCE(cou.coupon_amount, 0) as coupon_amount, " +
            "oi.order_item_name, oi.order_item_category, oi.order_item_quantity, oi.order_item_quantity_type, oi.order_item_unit_price, " +
            "oi.order_item_total_price, oi.order_item_image, oi.order_item_id, " +
            "cur.currency_name, cur.currency_sign " +
            "from orders o " +
            "left join currencies cur on cur.currency_id = o.fk_currency_id " +
            "left join order_items oi on o.order_id = oi.fk_order_id " +
            "left join delivery_costs dc on dc.delivery_cost_id = o.fk_delivery_cost_id " +
            "left join payment_types pt on o.fk_payment_type_id = pt.payment_type_id " +
            "left join deliveries d on o.order_id = d.fk_order_id " +
            "left join coupons cou on cou.coupon_id = o.fk_coupon_id " +
            "left join shops s on o.fk_shop_id = s.shop_id " +
            "left join customers c on o.fk_customer_id = c.customer_id " +
            "left join delivery_schedules ds on o.fk_delivery_schedule_id = ds.delivery_schedule_id " +
            "left join status_catalogues st on o.fk_status_id = st.status_id " +
            "where o.order_id = ?1", nativeQuery = true)
    OrderDetails findOrderDetailsById(Long id);
}
