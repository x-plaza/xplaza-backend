/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.dao.DeliveryCost;
import com.xplaza.backend.jpa.repository.DeliveryCostDAORepository;
import com.xplaza.backend.mapper.DeliveryCostMapper;
import com.xplaza.backend.service.entity.DeliveryCostEntity;

@Service
public class DeliveryCostService {
  @Autowired
  private DeliveryCostDAORepository deliveryCostDAORepo;
  @Autowired
  private DeliveryCostMapper deliveryCostMapper;

  public void addDeliveryCost(DeliveryCostEntity entity) {
    DeliveryCost dao = deliveryCostMapper.toDAO(entity);
    deliveryCostDAORepo.save(dao);
  }

  public void updateDeliveryCost(DeliveryCostEntity entity) {
    DeliveryCost dao = deliveryCostMapper.toDAO(entity);
    deliveryCostDAORepo.save(dao);
  }

  public String getDeliverySlabRangeNameByID(Long id) {
    // Implement if needed in DAO repo
    return null;
  }

  public void deleteDeliveryCost(Long id) {
    deliveryCostDAORepo.deleteById(id);
  }

  public List<DeliveryCostEntity> listDeliveryCosts() {
    return deliveryCostDAORepo.findAll().stream().map(deliveryCostMapper::toEntityFromDAO).collect(Collectors.toList());
  }

  public DeliveryCostEntity listDeliveryCost(Long id) {
    return deliveryCostDAORepo.findById(id).map(deliveryCostMapper::toEntityFromDAO).orElse(null);
  }
}
