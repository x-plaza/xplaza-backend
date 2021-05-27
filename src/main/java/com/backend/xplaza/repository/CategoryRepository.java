package com.backend.xplaza.repository;

import com.backend.xplaza.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "select category_name from categories where category_id = ?1", nativeQuery = true)
    String getName(Long id);

    @Query(value = "select * from categories where category_id = ?1", nativeQuery = true)
    Category findCategoryById(long id);
}
