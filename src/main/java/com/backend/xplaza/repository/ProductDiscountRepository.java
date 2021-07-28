package com.backend.xplaza.repository;

import com.backend.xplaza.model.ProductDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {
    @Query(value = "select concat(p.product_name,' ', p.product_var_type_value,' ', pvt.var_type_name) as product_name " +
            "from product_discounts pd " +
            "left join products p on pd.fk_product_id = p.product_id " +
            "left join product_variation_types pvt on p.fk_product_variation_type_id = pvt.product_variation_type " +
            "where pd.fk_product_id = ?1", nativeQuery = true)
    String getName(Long id);
}
