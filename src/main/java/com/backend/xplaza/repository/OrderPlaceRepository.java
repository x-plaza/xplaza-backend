package com.backend.xplaza.repository;

import com.backend.xplaza.model.OrderPlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderPlaceRepository extends JpaRepository<OrderPlace, Long> {
}
