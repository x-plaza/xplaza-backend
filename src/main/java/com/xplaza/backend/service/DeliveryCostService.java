/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.dao.DeliveryCostDao;
import com.xplaza.backend.jpa.repository.DeliveryCostRepository;
import com.xplaza.backend.mapper.DeliveryCostMapper;
import com.xplaza.backend.service.entity.DeliveryCost;

@Service
public class DeliveryCostService {
  @Autowired
  private DeliveryCostRepository deliveryCostRepo;
  @Autowired
  private DeliveryCostMapper deliveryCostMapper;

  public void addDeliveryCost(DeliveryCost entity) {
    DeliveryCostDao dao = deliveryCostMapper.toDao(entity);
    deliveryCostRepo.save(dao);
  }

  public void updateDeliveryCost(DeliveryCost entity) {
    DeliveryCostDao dao = deliveryCostMapper.toDao(entity);
    deliveryCostRepo.save(dao);
  }

  public String getDeliverySlabRangeNameByID(Long id) {
    // Implement if needed in DAO repo
    return null;
  }

  public void deleteDeliveryCost(Long id) {
    deliveryCostRepo.deleteById(id);
  }

  public List<DeliveryCost> listDeliveryCosts() {
    return deliveryCostRepo.findAll().stream().map(deliveryCostMapper::toEntityFromDao).toList();
  }

  public DeliveryCost listDeliveryCost(Long id) {
    return deliveryCostRepo.findById(id).map(deliveryCostMapper::toEntityFromDao).orElse(null);
  }
}
