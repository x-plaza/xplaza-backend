/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.model.ProductImage;
import com.xplaza.backend.repository.ProductImageRepository;

@Service
public class ProductImageService {
  @Autowired
  private ProductImageRepository productImgRepo;

  /*
   * public void addProductImage(ProductImage productImage) {
   * productImgRepo.save(productImage); }
   * 
   * public void updateProductImage(ProductImage productImage) {
   * productImgRepo.save(productImage); }
   * 
   * public List<ProductImage> listProductImages() { return
   * productImgRepo.findAll(Sort.by(Sort.Direction.ASC, "name")); }
   * 
   * public String getProductImageNameByID(Long id) { return
   * productImgRepo.getName(id); }
   * 
   * public void deleteProductImage(Long id) { productImgRepo.deleteById(id); }
   * 
   * public void deleteImagesByProductId(Long id) {
   * productImgRepo.deleteImagesByProductId(id); }
   */

  public List<ProductImage> listProductImage(Long id) {
    return productImgRepo.findImageByProductId(id);
  }
}
