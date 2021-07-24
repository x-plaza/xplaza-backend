package com.backend.xplaza.repository;

import com.backend.xplaza.model.ProductDiscount;
import com.backend.xplaza.model.ProductDiscountList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {
    @Query(value = "select concat(p.product_name,' ', p.product_var_type_value,' ', pvt.var_type_name) as product_name " +
            "from product_discounts pd " +
            "left join products p on pd.fk_product_id = p.product_id " +
            "left join product_variation_types pvt on p.fk_product_variation_type_id = pvt.product_variation_type " +
            "where pd.fk_product_id = ?1", nativeQuery = true)
    String getName(Long id);

    @Query(value = "select pd.*, concat(p.product_name,' ', p.product_var_type_value,' ', pvt.var_type_name) as product_name, dt.discount_type_name, c.currency_name, c.currency_sign " +
            "from product_discounts pd " +
            "left join products p on p.product_id = pd.fk_product_id " +
            "left join product_variation_types pvt on p.fk_product_variation_type_id = pvt.product_variation_type " +
            "left join discount_types dt on pd.fk_discount_type_id = dt.discount_type_id " +
            "left join currencies c on c.currency_id = pd.fk_currency_id " +
            "where pd.product_discount_id = ?1", nativeQuery = true)
    ProductDiscountList findProductDiscountById(Long id);

    @Query(value = "select pd.*, concat(p.product_name,' ', p.product_var_type_value,' ', pvt.var_type_name) as product_name, dt.discount_type_name, c.currency_name, c.currency_sign " +
            "from product_discounts pd " +
            "left join product_variation_types pvt on p.fk_product_variation_type_id = pvt.product_variation_type " +
            "left join products p on p.product_id = pd.fk_product_id " +
            "left join discount_types dt on pd.fk_discount_type_id = dt.discount_type_id " +
            "left join currencies c on c.currency_id = pd.fk_currency_id", nativeQuery = true)
    List<ProductDiscountList> findAllProductDiscounts();

    @Query(value = "select pd.*, concat(p.product_name,' ', p.product_var_type_value,' ', pvt.var_type_name) as product_name, dt.discount_type_name, c.currency_name, c.currency_sign " +
            "from product_discounts pd " +
            "left join products p on p.product_id = pd.fk_product_id " +
            "left join product_variation_types pvt on p.fk_product_variation_type_id = pvt.product_variation_type " +
            "left join discount_types dt on pd.fk_discount_type_id = dt.discount_type_id " +
            "left join currencies c on c.currency_id = pd.fk_currency_id " +
            "left join admin_user_shop_link ausl on p.fk_shop_id = ausl.shop_id " +
            "where ausl.admin_user_id = ?1", nativeQuery = true)
    List<ProductDiscountList> findAllProductDiscountByUserID(Long user_id);
}
