package com.backend.xplaza.repository;

import com.backend.xplaza.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select product_name from products where product_id = ?1", nativeQuery = true)
    String getName(Long id);

    @Query(value = "select * from products where product_id = ?1", nativeQuery = true)
    Product findProductById(Long id);
}
