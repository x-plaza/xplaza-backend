/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.model.DeliveryCost;
import com.xplaza.backend.model.DeliveryCostList;
import com.xplaza.backend.repository.DeliveryCostListRepository;
import com.xplaza.backend.repository.DeliveryCostRepository;

@Service
public class DeliveryCostService {
  @Autowired
  private DeliveryCostRepository deliveryCostRepo;
  @Autowired
  private DeliveryCostListRepository deliveryCostListRepo;

  public void addDeliveryCost(DeliveryCost deliveryCost) {
    deliveryCostRepo.save(deliveryCost);
  }

  public void updateDeliveryCost(DeliveryCost deliveryCost) {
    deliveryCostRepo.save(deliveryCost);
  }

  public String getDeliverySlabRangeNameByID(Long id) {
    return deliveryCostRepo.getName(id);
  }

  public void deleteDeliveryCost(Long id) {
    deliveryCostRepo.deleteById(id);
  }

//    public List<DeliveryCost> listDeliveryCost() {
//        return deliveryCostRepo.findAll();
//    }

  public List<DeliveryCostList> listDeliveryCosts() {
    return deliveryCostListRepo.findAllDeliveryCost();
  }

  public DeliveryCostList listDeliveryCost(Long id) {
    return deliveryCostListRepo.findDeliveryCostById(id);
  }
}
