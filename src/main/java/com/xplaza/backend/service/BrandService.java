/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.dao.BrandDao;
import com.xplaza.backend.jpa.repository.BrandRepository;
import com.xplaza.backend.mapper.BrandMapper;
import com.xplaza.backend.service.entity.Brand;

@Service
public class BrandService {
  @Autowired
  private BrandRepository brandRepo;

  @Autowired
  private BrandMapper brandMapper;

  public void addBrand(Brand entity) {
    BrandDao brand = brandMapper.toDao(entity);
    brandRepo.save(brand);
  }

  public void updateBrand(Brand entity) {
    BrandDao brand = brandMapper.toDao(entity);
    brandRepo.save(brand);
  }

  public String getBrandNameByID(Long id) {
    return brandRepo.getName(id);
  }

  public void deleteBrand(Long id) {
    brandRepo.deleteById(id);
  }

  public List<Brand> listBrands() {
    return brandRepo.findAll().stream().map(brandMapper::toEntityFromDAO).toList();
  }

  public Brand listBrand(Long id) {
    Brand dao = brandRepo.findBrandById(id);
    return brandMapper.toEntityFromDAO(dao);
  }

  public boolean isExist(Brand entity) {
    BrandDao brand = brandMapper.toDao(entity);
    return brandRepo.existsByName(brand.getName());
  }
}
