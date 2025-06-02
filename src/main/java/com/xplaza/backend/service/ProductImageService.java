/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.dao.City;
import com.xplaza.backend.jpa.repository.ProductImageDAORepository;
import com.xplaza.backend.mapper.ProductImageMapper;
import com.xplaza.backend.service.entity.ProductImageEntity;

@Service
public class ProductImageService {
  @Autowired
  private ProductImageDAORepository productImageDAORepo;
  @Autowired
  private ProductImageMapper productImageMapper;

  public void addProductImage(ProductImageEntity entity) {
    City.ProductImage dao = productImageMapper.toDAO(entity);
    productImageDAORepo.save(dao);
  }

  public void updateProductImage(ProductImageEntity entity) {
    City.ProductImage dao = productImageMapper.toDAO(entity);
    productImageDAORepo.save(dao);
  }

  public List<ProductImageEntity> listProductImages() {
    return productImageDAORepo.findAll().stream().map(productImageMapper::toEntityFromDAO).collect(Collectors.toList());
  }

  public List<ProductImageEntity> listProductImage(Long productId) {
    return productImageDAORepo.findByProductId(productId).stream().map(productImageMapper::toEntityFromDAO)
        .collect(Collectors.toList());
  }

  public void deleteProductImage(Long id) {
    productImageDAORepo.deleteById(id);
  }

  public void deleteImagesByProductId(Long productId) {
    productImageDAORepo.deleteByProductId(productId);
  }
}
