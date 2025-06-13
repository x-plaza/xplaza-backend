/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.jpa.dao.DiscountTypeDao;
import com.xplaza.backend.jpa.repository.DiscountTypeRepository;
import com.xplaza.backend.mapper.DiscountTypeMapper;
import com.xplaza.backend.service.entity.DiscountType;

@Service
public class DiscountTypeService {

  @Autowired
  private DiscountTypeRepository discountTypeRepository;

  @Autowired
  private DiscountTypeMapper discountTypeMapper;

  @Transactional
  public DiscountType addDiscountType(DiscountType discountType) {
    DiscountTypeDao discountTypeDao = discountTypeMapper.toDao(discountType);
    DiscountTypeDao savedDiscountTypeDao = discountTypeRepository.save(discountTypeDao);
    return discountTypeMapper.toEntityFromDao(savedDiscountTypeDao);
  }

  @Transactional
  public DiscountType updateDiscountType(Long id, DiscountType discountType) {
    DiscountTypeDao existingDiscountTypeDao = discountTypeRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Discount type not found with id: " + id));

    DiscountTypeDao discountTypeDao = discountTypeMapper.toDao(discountType);
    discountTypeDao.setDiscountTypeId(existingDiscountTypeDao.getDiscountTypeId());

    DiscountTypeDao updatedDiscountTypeDao = discountTypeRepository.save(discountTypeDao);
    return discountTypeMapper.toEntityFromDao(updatedDiscountTypeDao);
  }

  @Transactional
  public void deleteDiscountType(Long id) {
    discountTypeRepository.deleteById(id);
  }

  public List<DiscountType> listDiscountTypes() {
    List<DiscountTypeDao> discountTypeDaos = discountTypeRepository.findAll();
    return discountTypeDaos.stream()
        .map(discountTypeMapper::toEntityFromDao)
        .toList();
  }

  public DiscountType listDiscountType(Long id) {
    DiscountTypeDao discountTypeDao = discountTypeRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Discount type not found with id: " + id));
    return discountTypeMapper.toEntityFromDao(discountTypeDao);
  }
}
