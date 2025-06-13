/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.common.util.DateConverter;
import com.xplaza.backend.jpa.dao.ProductDao;
import com.xplaza.backend.jpa.dao.ProductDiscountDao;
import com.xplaza.backend.jpa.repository.ProductDiscountListRepository;
import com.xplaza.backend.jpa.repository.ProductDiscountRepository;
import com.xplaza.backend.jpa.repository.ProductRepository;
import com.xplaza.backend.mapper.ProductDiscountMapper;
import com.xplaza.backend.service.entity.ProductDiscount;
import com.xplaza.backend.service.entity.ProductDiscountList;

@Service
@Transactional
public class ProductDiscountService extends DateConverter {
  @Autowired
  private ProductDiscountRepository productDiscountRepository;
  @Autowired
  private ProductDiscountListRepository productDiscountListRepo;
  @Autowired
  private ProductRepository productRepo;
  @Autowired
  private ProductDiscountMapper productDiscountMapper;
  @Autowired
  private ProductDiscountListMapper productDiscountListMapper;

  public boolean checkDiscountValidity(ProductDiscount entity) {
    ProductDao product = productRepo.findProductById(entity.getProduct().getProductId());
    Double originalSellingPrice = product.getProductSellingPrice();
    if (entity.getDiscountAmount() > originalSellingPrice)
      return false;
    return true;
  }

  public ProductDiscount addProductDiscount(ProductDiscount productDiscount) {
    ProductDiscountDao productDiscountDao = productDiscountMapper.toDao(productDiscount);
    ProductDiscountDao savedProductDiscountDao = productDiscountRepository.save(productDiscountDao);
    return productDiscountMapper.toEntityFromDao(savedProductDiscountDao);
  }

  public ProductDiscount updateProductDiscount(Long id, ProductDiscount productDiscount) {
    ProductDiscountDao existingProductDiscountDao = productDiscountRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product discount not found with id: " + id));

    ProductDiscountDao productDiscountDao = productDiscountMapper.toDao(productDiscount);
    productDiscountDao.setProductDiscountId(existingProductDiscountDao.getProductDiscountId());

    ProductDiscountDao updatedProductDiscountDao = productDiscountRepository.save(productDiscountDao);
    return productDiscountMapper.toEntityFromDao(updatedProductDiscountDao);
  }

  public void deleteProductDiscount(Long id) {
    productDiscountRepository.deleteById(id);
  }

  public List<ProductDiscount> listProductDiscounts() {
    List<ProductDiscountDao> productDiscountDaos = productDiscountRepository.findAll();
    return productDiscountDaos.stream()
        .map(productDiscountMapper::toEntityFromDao)
        .toList();
  }

  public ProductDiscount listProductDiscount(Long id) {
    ProductDiscountDao productDiscountDao = productDiscountRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Product discount not found with id: " + id));
    return productDiscountMapper.toEntityFromDao(productDiscountDao);
  }

  public String getProductNameByID(Long id) {
    return productDiscountRepository.getName(id);
  }

  public List<ProductDiscountList> listProductDiscountsByProduct(Long productId) {
    return productDiscountListRepo.findByProductId(productId).stream()
        .map(productDiscountListMapper::toEntity)
        .collect(Collectors.toList());
  }

  public boolean checkDiscountDateValidity(ProductDiscount entity) {
    Date current_date = new Date();
    current_date = convertDateToStartOfTheDay(current_date);
    Date start_date = entity.getValidFrom();
    Date end_date = entity.getValidTo();
    if (current_date.after(start_date))
      return false;
    if (current_date.after(end_date))
      return false;
    if (start_date.after(end_date))
      return false;
    return true;
  }

  public ProductDiscount getProductDiscountByProduct(Long productId) {
    ProductDiscountDao productDiscountDao = productDiscountRepository.findByProductId(productId);
    return productDiscountMapper.toEntityFromDao(productDiscountDao);
  }
}
