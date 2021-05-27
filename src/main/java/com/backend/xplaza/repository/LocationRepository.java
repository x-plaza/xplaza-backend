package com.backend.xplaza.repository;

import com.backend.xplaza.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query(value = "select location_name from locations where location_id = ?1", nativeQuery = true)
    String getName(Long id);

    @Query(value = "select * from locations where location_id = ?1", nativeQuery = true)
    Location findLocationById(long id);
}
