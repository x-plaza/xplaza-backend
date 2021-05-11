package com.backend.xplaza.repository;

import com.backend.xplaza.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query(value = "select country_name from countries where country_id = ?1", nativeQuery = true)
    String getName(Long id);

    @Query(value = "select * from countries where country_id = ?1", nativeQuery = true)
    Country findItemById(long id);
}
