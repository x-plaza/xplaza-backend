/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.jpa.dao.ProductImageDao;
import com.xplaza.backend.jpa.repository.ProductImageRepository;
import com.xplaza.backend.mapper.ProductImageMapper;
import com.xplaza.backend.service.entity.ProductImage;

@Service
public class ProductImageService {

  @Autowired
  private ProductImageRepository productImageRepository;

  @Autowired
  private ProductImageMapper productImageMapper;

  @Transactional
  public ProductImage addProductImage(ProductImage productImage) {
    ProductImageDao productImageDao = productImageMapper.toDao(productImage);
    ProductImageDao savedProductImageDao = productImageRepository.save(productImageDao);
    return productImageMapper.toEntityFromDao(savedProductImageDao);
  }

  @Transactional
  public ProductImage updateProductImage(Long id, ProductImage productImage) {
    ProductImageDao existingProductImageDao = productImageRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product image not found with id: " + id));

    ProductImageDao productImageDao = productImageMapper.toDao(productImage);
    productImageDao.setProductImagesId(existingProductImageDao.getProductImagesId());
    productImageDao.setCreatedBy(existingProductImageDao.getCreatedBy());
    productImageDao.setCreatedAt(existingProductImageDao.getCreatedAt());

    ProductImageDao updatedProductImageDao = productImageRepository.save(productImageDao);
    return productImageMapper.toEntityFromDao(updatedProductImageDao);
  }

  @Transactional
  public void deleteProductImage(Long id) {
    productImageRepository.deleteById(id);
  }

  public List<ProductImage> listProductImages() {
    List<ProductImageDao> productImageDaos = productImageRepository.findAll();
    return productImageDaos.stream()
        .map(productImageMapper::toEntityFromDao)
        .toList();
  }

  public ProductImage listProductImage(Long id) {
    ProductImageDao productImageDao = productImageRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product image not found with id: " + id));
    return productImageMapper.toEntityFromDao(productImageDao);
  }

  public List<ProductImage> listProductImagesByProduct(Long productId) {
    List<ProductImageDao> productImageDaos = productImageRepository.findByProductId(productId);
    return productImageDaos.stream()
        .map(productImageMapper::toEntityFromDao)
        .toList();
  }
}
