/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.xplaza.model.DeliverySchedule;
import com.backend.xplaza.model.DeliveryScheduleDetails;
import com.backend.xplaza.model.DeliveryScheduleList;
import com.backend.xplaza.repository.DeliveryScheduleDetailsRepository;
import com.backend.xplaza.repository.DeliveryScheduleListRepository;
import com.backend.xplaza.repository.DeliveryScheduleRepository;

@Service
public class DeliveryScheduleService {

  @Autowired
  private DeliveryScheduleRepository deliveryScheduleRepo;
  @Autowired
  private DeliveryScheduleDetailsRepository deliveryScheduleDetailsRepo;

  @Autowired
  private DeliveryScheduleListRepository deliveryScheduleListRepo;

  public void addSchedule(DeliverySchedule deliverySchedule) {
    deliveryScheduleRepo.save(deliverySchedule);
  }

  public void updateSchedule(DeliverySchedule deliverySchedule) {
    deliveryScheduleRepo.save(deliverySchedule);
  }

  public List<DeliveryScheduleList> listDeliverySchedules() {
    return deliveryScheduleListRepo.findAllItem();
  }

  public DeliveryScheduleDetails listDeliveryScheduleDetails(Long id) {
    return deliveryScheduleDetailsRepo.findDeliveryScheduleDetailsById(id);
  }

  public void deleteSchedule(Long id) {
    deliveryScheduleRepo.deleteById(id);
  }
}
