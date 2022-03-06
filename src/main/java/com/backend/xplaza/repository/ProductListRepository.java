package com.backend.xplaza.repository;

import com.backend.xplaza.model.ProductList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductListRepository extends JpaRepository<ProductList, Long> {
    @Query(value = "select pd.discount_amount, dt.discount_type_name, pd.discount_start_date, pd.discount_end_date, " +
            "null as discounted_price, p.*, b.brand_name, s.shop_name, c.category_name, var.var_type_name, cur.currency_name, cur.currency_sign " +
            "from products p " +
            "left join product_discounts pd on p.product_id = pd.fk_product_id \n" +
            "left join discount_types dt on dt.discount_type_id = pd.fk_discount_type_id \n" +
            "left join brands b on p.fk_brand_id = b.brand_id " +
            "left join shops s on p.fk_shop_id = s.shop_id " +
            "left join categories c on p.fk_category_id = c.category_id " +
            "left join product_variation_types var on p.fk_product_var_type_id = var.product_var_type_id " +
            "left join currencies cur on p.fk_currency_id = cur.currency_id " +
            "where product_id = ?1", nativeQuery = true)
    ProductList findProductListById(Long id);

    @Query(value = "select pd.discount_amount, dt.discount_type_name, pd.discount_start_date, pd.discount_end_date, " +
            "null as discounted_price, p.*, b.brand_name, s.shop_name, c.category_name, var.var_type_name, cur.currency_name, cur.currency_sign " +
            "from products p " +
            "left join product_discounts pd on p.product_id = pd.fk_product_id \n" +
            "left join discount_types dt on dt.discount_type_id = pd.fk_discount_type_id \n" +
            "left join brands b on p.fk_brand_id = b.brand_id " +
            "left join shops s on p.fk_shop_id = s.shop_id " +
            "left join categories c on p.fk_category_id = c.category_id " +
            "left join product_variation_types var on p.fk_product_var_type_id = var.product_var_type_id " +
            "left join currencies cur on p.fk_currency_id = cur.currency_id", nativeQuery = true)
    List<ProductList> findAllProductList();

    @Query(value = "select pd.discount_amount, dt.discount_type_name, pd.discount_start_date, pd.discount_end_date, " +
            "null as discounted_price, p.*, b.brand_name, s.shop_name, c.category_name, var.var_type_name, cur.currency_name, cur.currency_sign " +
            "from products p " +
            "left join product_discounts pd on p.product_id = pd.fk_product_id \n" +
            "left join discount_types dt on dt.discount_type_id = pd.fk_discount_type_id \n" +
            "left join brands b on p.fk_brand_id = b.brand_id " +
            "left join shops s on p.fk_shop_id = s.shop_id " +
            "left join categories c on p.fk_category_id = c.category_id " +
            "left join product_variation_types var on p.fk_product_var_type_id = var.product_var_type_id " +
            "left join currencies cur on p.fk_currency_id = cur.currency_id " +
            "left join admin_user_shop_link ausl on ausl.shop_id = s.shop_id " +
            "where ausl.admin_user_id = ?1", nativeQuery = true)
    List<ProductList> findAllProductListByUserID(Long user_id);

    @Query(value = "select pd.discount_amount, dt.discount_type_name, pd.discount_start_date, pd.discount_end_date, " +
            "null as discounted_price, p.*, b.brand_name, s.shop_name,\n" +
            "c.category_name, var.var_type_name, cur.currency_name, cur.currency_sign \n" +
            "from products p \n" +
            "left join product_discounts pd on p.product_id = pd.fk_product_id \n" +
            "left join discount_types dt on dt.discount_type_id = pd.fk_discount_type_id \n" +
            "left join brands b on p.fk_brand_id = b.brand_id \n" +
            "left join shops s on p.fk_shop_id = s.shop_id \n" +
            "left join categories c on p.fk_category_id = c.category_id \n" +
            "left join product_variation_types var on p.fk_product_var_type_id = var.product_var_type_id \n" +
            "left join currencies cur on p.fk_currency_id = cur.currency_id \n" +
            "where  s.shop_id = ?1", nativeQuery = true)
    List<ProductList> findAllProductListByShopID(Long shop_id);

    @Query(value = "select pd.discount_amount, dt.discount_type_name, pd.discount_start_date, pd.discount_end_date, " +
            "null as discounted_price, p.*, b.brand_name, s.shop_name,\n" +
            "c.category_name, var.var_type_name, cur.currency_name, cur.currency_sign \n" +
            "from products p \n" +
            "left join product_discounts pd on p.product_id = pd.fk_product_id \n" +
            "left join discount_types dt on dt.discount_type_id = pd.fk_discount_type_id \n" +
            "left join brands b on p.fk_brand_id = b.brand_id \n" +
            "left join shops s on p.fk_shop_id = s.shop_id \n" +
            "left join categories c on p.fk_category_id = c.category_id \n" +
            "left join product_variation_types var on p.fk_product_var_type_id = var.product_var_type_id \n" +
            "left join currencies cur on p.fk_currency_id = cur.currency_id \n" +
            "where  s.shop_id = ?1 and c.category_id = ?2", nativeQuery = true)
    List<ProductList> findAllProductListByCategory(Long shop_id, Long category_id);

    @Query(value = "select p.*, b.brand_name, s.shop_name, pd.discount_amount, dt.discount_type_name, pd.discount_start_date, pd.discount_end_date, \n" +
            "null as discounted_price,\n" +
            "c.category_name, var.var_type_name, cur.currency_name, cur.currency_sign \n" +
            "from products p \n" +
            "left join product_discounts pd on p.product_id = pd.fk_product_id \n" +
            "left join discount_types dt on dt.discount_type_id = pd.fk_discount_type_id \n" +
            "left join brands b on p.fk_brand_id = b.brand_id \n" +
            "left join shops s on p.fk_shop_id = s.shop_id \n" +
            "left join categories c on p.fk_category_id = c.category_id \n" +
            "left join product_variation_types var on p.fk_product_var_type_id = var.product_var_type_id \n" +
            "left join currencies cur on p.fk_currency_id = cur.currency_id \n" +
            "where  s.shop_id = ?1 and p.product_id in (\n" +
            "select product_id\n" +
            "from order_items oi \n" +
            "group by product_id \n" +
            "order by count(*) desc)", nativeQuery = true)
    List<ProductList> findAllProductListByTrending(Long shop_id);
}