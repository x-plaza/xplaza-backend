/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.RevenueDao;

public interface DashboardRepository extends JpaRepository<RevenueDao, Long> {
  @Query(value = "select r.* from revenue r where r.shop_id = ?1", nativeQuery = true)
  RevenueDao findAllDetailsByShopId(Long shopId);

  @Query(value = "select coalesce (sum(order_item_total_price) - sum(product_buying_price), 0) as monthly_profit " +
      "from order_items oi " +
      "left join orders o on o.order_id = oi.fk_order_id " +
      "where o.shop_id = ?1 " +
      "and o.fk_status_id = 5 " +
      "and o.date_to_deliver >= TO_DATE(?2, 'YYYY-MM-01') " +
      "and o.date_to_deliver <= TO_DATE(?2, 'YYYY-MM-01') + interval '1 month - 1 day'", nativeQuery = true)
  Double findProfitByMonthByShopId(Long shopId, String dateOfAMonth);

  @Query(value = "select coalesce (sum(o.total_price) - sum(o.discount_amount), 0) as monthly_sales " +
      "from orders o " +
      "where o.shop_id = ?1 " +
      "and o.fk_status_id = 5 " +
      "and o.date_to_deliver >= TO_DATE(?2, 'YYYY-MM-01') " +
      "and o.date_to_deliver <= TO_DATE(?2, 'YYYY-MM-01') + interval '1 month - 1 day'", nativeQuery = true)
  Double findSalesByMonthByShopId(Long shopId, String dateOfAMonth);
}
