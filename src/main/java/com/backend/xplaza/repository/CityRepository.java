package com.backend.xplaza.repository;

import com.backend.xplaza.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CityRepository extends JpaRepository<City, Long> {
    @Query(value = "select city_name from cities where city_id = ?1", nativeQuery = true)
    String getName(Long id);

    @Query(value = "select * from cities where city_id = ?1", nativeQuery = true)
    City findCityById(Long id);
}
