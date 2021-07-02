package com.backend.xplaza.repository;

import com.backend.xplaza.model.ProductList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductListRepository extends JpaRepository<ProductList, Long> {
    @Query(value = "select p.*, b.brand_name, s.shop_name, c.category_name, var.var_type_name, cur.currency_name " +
            "from products p " +
            "left join brands b on p.fk_brand_id = b.brand_id " +
            "left join shops s on p.fk_shop_id = s.shop_id " +
            "left join categories c on p.fk_category_id = c.category_id " +
            "left join product_variation_types var on p.fk_product_var_type_id = var.product_var_type_id " +
            "left join currencies cur on p.fk_currency_id = cur.currency_id " +
            "where product_id = ?1", nativeQuery = true)
    ProductList findProductListById(Long id);

    @Query(value = "select p.*, b.brand_name, s.shop_name, c.category_name, var.var_type_name, cur.currency_name " +
            "from products p " +
            "left join brands b on p.fk_brand_id = b.brand_id " +
            "left join shops s on p.fk_shop_id = s.shop_id " +
            "left join categories c on p.fk_category_id = c.category_id " +
            "left join product_variation_types var on p.fk_product_var_type_id = var.product_var_type_id " +
            "left join currencies cur on p.fk_currency_id = cur.currency_id", nativeQuery = true)
    List<ProductList> findAllProductList();
}