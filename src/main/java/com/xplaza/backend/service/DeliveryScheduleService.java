/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.dao.DeliverySchedule;
import com.xplaza.backend.jpa.repository.DeliveryScheduleDAORepository;
import com.xplaza.backend.mapper.DeliveryScheduleMapper;
import com.xplaza.backend.service.entity.DeliveryScheduleEntity;

@Service
public class DeliveryScheduleService {

  @Autowired
  private DeliveryScheduleDAORepository deliveryScheduleDAORepo;
  @Autowired
  private DeliveryScheduleMapper deliveryScheduleMapper;

  public void addSchedule(DeliveryScheduleEntity entity) {
    DeliverySchedule dao = deliveryScheduleMapper.toDAO(entity);
    deliveryScheduleDAORepo.save(dao);
  }

  public void updateSchedule(DeliveryScheduleEntity entity) {
    DeliverySchedule dao = deliveryScheduleMapper.toDAO(entity);
    deliveryScheduleDAORepo.save(dao);
  }

  public List<DeliveryScheduleEntity> listDeliverySchedules() {
    return deliveryScheduleDAORepo.findAll().stream().map(deliveryScheduleMapper::toEntityFromDAO)
        .collect(Collectors.toList());
  }

  public DeliveryScheduleEntity listDeliverySchedule(Long id) {
    return deliveryScheduleDAORepo.findById(id).map(deliveryScheduleMapper::toEntityFromDAO).orElse(null);
  }

  public void deleteSchedule(Long id) {
    deliveryScheduleDAORepo.deleteById(id);
  }
}
