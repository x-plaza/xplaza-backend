/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.model.DeliverySchedule;
import com.xplaza.backend.model.DeliveryScheduleDetails;
import com.xplaza.backend.model.DeliveryScheduleList;
import com.xplaza.backend.repository.DeliveryScheduleDetailsRepository;
import com.xplaza.backend.repository.DeliveryScheduleListRepository;
import com.xplaza.backend.repository.DeliveryScheduleRepository;

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
