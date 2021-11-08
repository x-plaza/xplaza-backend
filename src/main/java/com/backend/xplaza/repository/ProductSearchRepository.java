package com.backend.xplaza.repository;

import com.backend.xplaza.model.ProductSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductSearchRepository extends JpaRepository<ProductSearch, Long> {
    @Query(value = "select p.product_id, p.product_name from products p \n" +
            "where p.fk_shop_id = ?1 and p.product_name iLIKE %?2% \n" +
            "ORDER BY p.product_name", nativeQuery = true)
    List<ProductSearch> findProductListByName(Long shop_id, String product_name);
}
