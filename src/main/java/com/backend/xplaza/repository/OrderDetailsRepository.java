package com.backend.xplaza.repository;

import com.backend.xplaza.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    @Query(value = "select o.order_id, o.total_price, o.discount_amount, o.grand_total_price, o.delivery_address, concat(c.first_name,' ',c.last_name) as customer_name, " +
            "c.mobile_no, o.received_time, o.fk_shop_id, s.shop_name, o.fk_status_id, st.status_name, o.fk_delivery_schedule_id, " +
            "concat(ds.delivery_schedule_start, '-' , ds.delivery_schedule_end) as allotted_time,  o.fk_delivery_cost_id, dc.delivery_cost, " +
            "o.fk_payment_type_id, pt.payment_type_name, o.fk_delivery_id, d.person_name, d.contact_no, " +
            "o.fk_coupon_id, cou.coupon_code, cou.coupon_amount, " +
            "oi.order_item_name, oi.order_item_category, oi.order_item_quantity, oi.order_item_quantity_type, oi.order_item_unit_price, " +
            "oi.order_item_total_price, oi.order_item_image " +
            "from orders o " +
            "left join order_items oi on o.order_id = oi.fk_order_id " +
            "left join delivery_costs dc on dc.delivery_cost_id = o.fk_delivery_cost_id " +
            "left join payment_types pt on o.fk_payment_type_id = pt.payment_type_id " +
            "left join deliveries d on o.fk_delivery_id = d.delivery_id " +
            "left join coupons cou on cou.coupon_id = o.fk_coupon_id " +
            "left join shops s on o.fk_shop_id = s.shop_id " +
            "left join customers c on o.fk_customer_id = c.customer_id " +
            "left join delivery_schedules ds on o.fk_delivery_schedule_id = ds.delivery_schedule_id " +
            "left join status_catalogues st on o.fk_status_id = st.status_id " +
            "where o.order_id = ?1", nativeQuery = true)
    OrderDetails findOrderDetailsById(long id);
}
