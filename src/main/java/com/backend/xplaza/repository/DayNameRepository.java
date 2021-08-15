package com.backend.xplaza.repository;

import com.backend.xplaza.model.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DayNameRepository extends JpaRepository<Day, Long>{
    @Query(value = "select day_name from day_names where day_id = ?1", nativeQuery = true)
    String getName(Long id);
}
