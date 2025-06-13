/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.jpa.dao.ProductVariationTypeDao;
import com.xplaza.backend.jpa.repository.ProductVariationTypeRepository;
import com.xplaza.backend.mapper.ProductVariationTypeMapper;
import com.xplaza.backend.service.entity.ProductVariationType;

@Service
@Transactional
public class ProductVariationTypeService {

  @Autowired
  private ProductVariationTypeRepository productVariationTypeRepository;

  @Autowired
  private ProductVariationTypeMapper productVariationTypeMapper;

  public ProductVariationType addProductVariationType(ProductVariationType productVariationType) {
    ProductVariationTypeDao productVariationTypeDao = productVariationTypeMapper.toDao(productVariationType);
    ProductVariationTypeDao savedProductVariationTypeDao = productVariationTypeRepository.save(productVariationTypeDao);
    return productVariationTypeMapper.toEntityFromDao(savedProductVariationTypeDao);
  }

  public ProductVariationType updateProductVariationType(ProductVariationType productVariationType) {
    ProductVariationTypeDao existingProductVariationTypeDao = productVariationTypeRepository
        .findById(productVariationType.getProductVarTypeId())
        .orElseThrow(() -> new RuntimeException(
            "Product variation type not found with id: " + productVariationType.getProductVarTypeId()));

    ProductVariationTypeDao productVariationTypeDao = productVariationTypeMapper.toDao(productVariationType);
    productVariationTypeDao.setProductVariationTypeId(existingProductVariationTypeDao.getProductVariationTypeId());

    ProductVariationTypeDao updatedProductVariationTypeDao = productVariationTypeRepository
        .save(productVariationTypeDao);
    return productVariationTypeMapper.toEntityFromDao(updatedProductVariationTypeDao);
  }

  public void deleteProductVariationType(Long id) {
    productVariationTypeRepository.deleteById(id);
  }

  public List<ProductVariationType> listProductVariationTypes() {
    List<ProductVariationTypeDao> productVariationTypeDaos = productVariationTypeRepository.findAll();
    return productVariationTypeDaos.stream()
        .map(productVariationTypeMapper::toEntityFromDao)
        .toList();
  }

  public ProductVariationType listProductVariationType(Long id) {
    ProductVariationTypeDao productVariationTypeDao = productVariationTypeRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product variation type not found with id: " + id));
    return productVariationTypeMapper.toEntityFromDao(productVariationTypeDao);
  }
}
