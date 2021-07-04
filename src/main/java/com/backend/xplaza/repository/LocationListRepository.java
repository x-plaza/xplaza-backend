package com.backend.xplaza.repository;

import com.backend.xplaza.model.LocationList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationListRepository extends JpaRepository<LocationList, Long> {
    @Query(value = "select l.*, c.city_name from locations l left join cities c on l.fk_city_id = c.city_id", nativeQuery = true)
    List<LocationList> findAllItem();

    @Query(value = "select l.*, c.city_name from locations l left join cities c on l.fk_city_id = c.city_id where l.location_id = ?1", nativeQuery = true)
    LocationList findLocationListById(long id);
}
