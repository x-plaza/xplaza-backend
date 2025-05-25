/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.backend.xplaza.model.DiscountType;
import com.backend.xplaza.repository.DiscountTypeRepository;

@Service
public class DiscountTypeService {
  @Autowired
  private DiscountTypeRepository discountTypeRepo;

  public List<DiscountType> listDiscountTypes() {
    return discountTypeRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
  }
}
