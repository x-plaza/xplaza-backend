package com.backend.xplaza.repository;

import com.backend.xplaza.model.ProductVarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductVarTypeRepository extends JpaRepository<ProductVarType, Long> {
    @Query(value = "select var_type_name from product_variation_types where product_var_type_id = ?1", nativeQuery = true)
    String getName(long id);

    @Query(value = "select * from product_variation_types where product_var_type_id = ?1", nativeQuery = true)
    ProductVarType findProdVarTypeById(long id);
}
