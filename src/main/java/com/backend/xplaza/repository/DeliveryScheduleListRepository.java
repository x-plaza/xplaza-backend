package com.backend.xplaza.repository;

import com.backend.xplaza.model.DeliveryScheduleList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryScheduleListRepository extends JpaRepository<DeliveryScheduleList, Long> {
    @Query(value = "select dn.* from day_names dn", nativeQuery = true)
    List<DeliveryScheduleList> findAllItem();

    @Query(value = "select dn.* from day_names dn where dn.day_id = ?1", nativeQuery = true)
    DeliveryScheduleList findDeliveryScheduleListByDayId(Long id);
}
