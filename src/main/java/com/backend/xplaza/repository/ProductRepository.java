package com.backend.xplaza.repository;

import com.backend.xplaza.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select product_name from products where product_id = ?1", nativeQuery = true)
    String getName(Long id);

    @Query(value = "select * from products where product_id = ?1", nativeQuery = true)
    Product findProductById(Long id);

    @Modifying
    @Transactional
    @Query(value = "update products set quantity = ?2 where product_id = ?1", nativeQuery = true)
    Product updateProductInventory(Long id, Long new_quantity);
}
