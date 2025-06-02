/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.jpa.dao.Product;
import com.xplaza.backend.jpa.repository.ProductDAORepository;
import com.xplaza.backend.jpa.repository.ProductListDAORepository;
import com.xplaza.backend.mapper.ProductListMapper;
import com.xplaza.backend.mapper.ProductMapper;
import com.xplaza.backend.service.entity.ProductEntity;
import com.xplaza.backend.service.entity.ProductListEntity;

@Service
public class ProductService {
  @Autowired
  private ProductDAORepository productDAORepo;
  @Autowired
  private ProductListDAORepository productListDAORepo;
  @Autowired
  private ProductMapper productMapper;
  @Autowired
  private ProductListMapper productListMapper;

  @Transactional
  public void addProduct(ProductEntity productEntity) {
    Product dao = productMapper.toDAO(productEntity);
    productDAORepo.save(dao);
    // handle images if needed
  }

  @Transactional
  public void updateProduct(ProductEntity productEntity) {
    Product dao = productMapper.toDAO(productEntity);
    productDAORepo.save(dao);
    // handle images if needed
  }

  @Transactional
  public void deleteProduct(Long id) {
    productDAORepo.deleteById(id);
    // handle images if needed
  }

  public String getProductNameByID(Long id) {
    // Implement if needed in DAO repo
    return null;
  }

  public List<ProductListEntity> listProducts() {
    List<ProductListDAO> daos = productListDAORepo.findAll();
    return daos.stream().map(productListMapper::toEntity).toList();
  }

  public List<ProductListEntity> listProductsByUserID(Long userId) {
    List<ProductListDAO> daos = productListDAORepo.findAll(); // replace with correct query
    return daos.stream().map(productListMapper::toEntity).toList();
  }

  public ProductListEntity listProduct(Long id) {
    ProductListDAO dao = productListDAORepo.findById(id).orElse(null);
    return productListMapper.toEntity(dao);
  }

  public List<ProductListEntity> listProductsByShopIDByAdmin(Long shopId) {
    List<ProductListDAO> daos = productListDAORepo.findAll(); // replace with correct query
    return daos.stream().map(productListMapper::toEntity).toList();
  }

  public List<ProductListEntity> listProductsByShopID(Long shopId) {
    List<ProductListDAO> daos = productListDAORepo.findAll(); // replace with correct query
    return daos.stream().map(productListMapper::toEntity).toList();
  }

  public List<ProductListEntity> listProductsByCategory(Long shopId, Long categoryId) {
    List<ProductListDAO> daos = productListDAORepo.findAll(); // replace with correct query
    return daos.stream().map(productListMapper::toEntity).toList();
  }

  public List<ProductListEntity> listProductsByTrending(Long shopId) {
    List<ProductListDAO> daos = productListDAORepo.findAll(); // replace with correct query
    return daos.stream().map(productListMapper::toEntity).toList();
  }
}
