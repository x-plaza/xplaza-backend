/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xplaza.backend.jpa.dao.ProductDiscountDao;

public interface ProductDiscountRepository extends JpaRepository<ProductDiscountDao, Long> {
  @Query(value = "select concat(p.product_name,' (', p.product_var_type_value,' ', pvt.var_type_name, ')') as product_name \n"
      +
      "from product_discounts pd \n" +
      "left join products p on pd.fk_product_id = p.product_id \n" +
      "left join product_variation_types pvt on p.fk_product_var_type_id = pvt.product_var_type_id \n" +
      "where pd.product_discount_id = ?1", nativeQuery = true)
  String getName(Long id);

  @Query(value = "SELECT SUM(discount_amount) AS discount_amount \n" +
      "from (\n" +
      "select coalesce(discount_amount, 0.0) as discount_amount \n" +
      "from product_discounts \n" +
      "where fk_product_id = ?1 and now() between discount_start_date and discount_end_date\n" +
      "UNION ALL SELECT 0 AS discount_amount) as forcedrow", nativeQuery = true)
  Double findProductDiscountByProductId(Long product_id);

  @Query(value = "select pd.* from product_discounts pd where fk_product_id = ?1 and now() between pd.discount_start_date and pd.discount_end_date", nativeQuery = true)
  ProductDiscountDao findByProductId(Long product_id);
}
