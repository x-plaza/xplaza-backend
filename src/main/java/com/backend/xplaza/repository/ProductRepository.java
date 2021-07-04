package com.backend.xplaza.repository;

import com.backend.xplaza.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select product_name from products where product_id = ?1", nativeQuery = true)
    String getName(long id);

    @Query(value = "select * from products where product_id = ?1", nativeQuery = true)
    Product findProductById(long id);

    @Query(value = "SELECT product_id \n" +
            "FROM products \n" +
            "ORDER BY product_id DESC \n" +
            "LIMIT 1", nativeQuery = true)
    long getMaxID();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM product_images WHERE fk_product_id = ?1", nativeQuery = true)
    void deleteImagesByProductId(long id);
}
