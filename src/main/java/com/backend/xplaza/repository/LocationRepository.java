package com.backend.xplaza.repository;

import com.backend.xplaza.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
