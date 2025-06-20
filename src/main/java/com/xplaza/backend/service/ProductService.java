/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.jpa.dao.ProductDao;
import com.xplaza.backend.jpa.repository.ProductRepository;
import com.xplaza.backend.mapper.ProductMapper;
import com.xplaza.backend.service.entity.Product;

@Service
public class ProductService {
  private final ProductRepository productRepo;
  private final ProductMapper productMapper;

  @Autowired
  public ProductService(ProductRepository productRepo, ProductMapper productMapper) {
    this.productRepo = productRepo;
    this.productMapper = productMapper;
  }

  @Transactional
  public Product addProduct(Product product) {
    ProductDao productDao = productMapper.toDao(product);
    ProductDao savedProductDao = productRepo.save(productDao);
    return productMapper.toEntityFromDao(savedProductDao);
  }

  @Transactional
  public Product updateProduct(Product product) {
    productRepo.findById(product.getProductId())
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + product.getProductId()));
    ProductDao productDao = productMapper.toDao(product);
    ProductDao updatedProductDao = productRepo.save(productDao);
    return productMapper.toEntityFromDao(updatedProductDao);
  }

  @Transactional
  public void deleteProduct(Long id) {
    productRepo.deleteById(id);
  }

  public List<Product> listProducts() {
    List<ProductDao> productDaos = productRepo.findAll();
    return productDaos.stream()
        .map(productMapper::toEntityFromDao)
        .collect(Collectors.toList());
  }

  public Product listProduct(Long id) {
    ProductDao productDao = productRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    return productMapper.toEntityFromDao(productDao);
  }

  public List<Product> listProductsByShop(Long shopId) {
    List<ProductDao> productDaos = productRepo.findByShopId(shopId);
    return productDaos.stream()
        .map(productMapper::toEntityFromDao)
        .collect(Collectors.toList());
  }

  public List<Product> listProductsByCategory(Long categoryId) {
    List<ProductDao> productDaos = productRepo.findByCategoryId(categoryId);
    return productDaos.stream()
        .map(productMapper::toEntityFromDao)
        .collect(Collectors.toList());
  }

  public List<Product> listProductsByBrand(Long brandId) {
    List<ProductDao> productDaos = productRepo.findByBrandId(brandId);
    return productDaos.stream()
        .map(productMapper::toEntityFromDao)
        .collect(Collectors.toList());
  }

  public String getProductNameByID(Long id) {
    return productRepo.getName(id);
  }

  @Transactional
  public void updateProductInventory(Long id, int quantity) {
    productRepo.updateInventory(id, quantity);
  }
}
