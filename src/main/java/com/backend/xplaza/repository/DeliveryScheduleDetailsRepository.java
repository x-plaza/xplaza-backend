package com.backend.xplaza.repository;

import com.backend.xplaza.model.DeliveryScheduleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryScheduleDetailsRepository extends JpaRepository<DeliveryScheduleDetails, Long> {
    @Query(value = "select ds.*,dn.day_name from delivery_schedules ds \n" +
            "left join day_names dn on ds.fk_day_id = dn.day_id \n" +
            "where ds.delivery_schedule_id = ?1", nativeQuery = true)
    DeliveryScheduleDetails findDeliveryScheduleDetailsById(Long id);
}
