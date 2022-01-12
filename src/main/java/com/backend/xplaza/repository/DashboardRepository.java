package com.backend.xplaza.repository;

import com.backend.xplaza.model.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    @Query(value = "select r.* from revenue r where r.shop_id = ?1", nativeQuery = true)
    Dashboard findAllDetailsByShopId(Long shop_id);

    @Query(value = "select coalesce (sum(order_item_total_price) - sum(product_buying_price), 0) as monthly_profit " +
            "from order_items oi " +
            "left join orders o on o.order_id = oi.fk_order_id " +
            "where o.shop_id = ?1 " +
            "and o.fk_status_id = 5 " +
            "and o.date_to_deliver >= TO_DATE(?2, 'YYYY-MM-01') " +
            "and o.date_to_deliver <= TO_DATE(?2, 'YYYY-MM-01') + interval '1 month - 1 day'", nativeQuery = true)
    Double findProfitByMonthByShopId(Long shop_id, String date_of_a_month);
}
