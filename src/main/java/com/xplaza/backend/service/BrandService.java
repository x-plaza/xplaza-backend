/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.model.Brand;
import com.xplaza.backend.repository.BrandRepository;

@Service
public class BrandService {
  @Autowired
  private BrandRepository brandRepo;

  public void addBrand(Brand brand) {
    brandRepo.save(brand);
  }

  public void updateBrand(Brand brand) {
    brandRepo.save(brand);
  }

  public String getBrandNameByID(Long id) {
    return brandRepo.getName(id);
  }

  public void deleteBrand(Long id) {
    brandRepo.deleteById(id);
  }

  public List<Brand> listBrands() {
    return brandRepo.findAll();
  }

  public Brand listBrand(Long id) {
    return brandRepo.findBrandById(id);
  }

  public boolean isExist(Brand brand) {
    return brandRepo.existsByName(brand.getName());
  }
}
