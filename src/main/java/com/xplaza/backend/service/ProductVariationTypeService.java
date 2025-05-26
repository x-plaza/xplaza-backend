/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.xplaza.backend.model.ProductVariationType;
import com.xplaza.backend.repository.ProductVariationTypeRepository;

@Service
public class ProductVariationTypeService {
  @Autowired
  private ProductVariationTypeRepository prodVarTypeRepo;

  public void addProductVarType(ProductVariationType productVariationType) {
    prodVarTypeRepo.save(productVariationType);
  }

  public void updateProductVarType(ProductVariationType productVariationType) {
    prodVarTypeRepo.save(productVariationType);
  }

  public List<ProductVariationType> listProductVarTypes() {
    return prodVarTypeRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
  }

  public String getProductVarTypeNameByID(Long id) {
    return prodVarTypeRepo.getName(id);
  }

  public void deleteProductVarType(Long id) {
    prodVarTypeRepo.deleteById(id);
  }

  public ProductVariationType listProductVarType(Long id) {
    return prodVarTypeRepo.findProdVarTypeById(id);
  }
}
