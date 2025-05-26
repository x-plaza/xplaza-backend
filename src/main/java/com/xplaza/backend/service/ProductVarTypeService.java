/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.xplaza.backend.model.ProductVarType;
import com.xplaza.backend.repository.ProductVarTypeRepository;

@Service
public class ProductVarTypeService {
  @Autowired
  private ProductVarTypeRepository prodVarTypeRepo;

  public void addProductVarType(ProductVarType productVarType) {
    prodVarTypeRepo.save(productVarType);
  }

  public void updateProductVarType(ProductVarType productVarType) {
    prodVarTypeRepo.save(productVarType);
  }

  public List<ProductVarType> listProductVarTypes() {
    return prodVarTypeRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
  }

  public String getProductVarTypeNameByID(Long id) {
    return prodVarTypeRepo.getName(id);
  }

  public void deleteProductVarType(Long id) {
    prodVarTypeRepo.deleteById(id);
  }

  public ProductVarType listProductVarType(Long id) {
    return prodVarTypeRepo.findProdVarTypeById(id);
  }
}
