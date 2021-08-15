package com.backend.xplaza.repository;

import com.backend.xplaza.model.DeliverySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryScheduleRepository extends JpaRepository<DeliverySchedule, Long> {
    @Query(value = "select * from delivery_schedules where delivery_schedule_id = ?1", nativeQuery = true)
    DeliverySchedule findDeliveryScheduleById(Long id);
}
