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

import com.xplaza.backend.common.util.DateConverter;
import com.xplaza.backend.dto.ProductDiscountRequestDTO;
import com.xplaza.backend.dto.ProductDiscountResponseDTO;
import com.xplaza.backend.entity.ProductDiscountEntity;
import com.xplaza.backend.mapper.ProductDiscountMapper;
import com.xplaza.backend.model.Product;
import com.xplaza.backend.model.ProductDiscount;
import com.xplaza.backend.repository.ProductDiscountListRepository;
import com.xplaza.backend.repository.ProductDiscountRepository;
import com.xplaza.backend.repository.ProductRepository;

@Service
public class ProductDiscountService extends DateConverter {
  @Autowired
  private ProductDiscountRepository productDiscountRepo;
  @Autowired
  private ProductDiscountListRepository productDiscountListRepo;
  @Autowired
  private ProductRepository productRepo;
  @Autowired
  private ProductDiscountMapper productDiscountMapper;

  public boolean checkDiscountValidity(ProductDiscountRequestDTO dto) {
    Product product = productRepo.findProductById(dto.getProductId());
    Double original_selling_price = product.getSelling_price();
    if (dto.getDiscountAmount() > original_selling_price)
      return false;
    return true;
  }

  public void addProductDiscount(ProductDiscountRequestDTO dto) {
    ProductDiscountEntity entity = productDiscountMapper.toEntity(dto);
    ProductDiscount productDiscount = new ProductDiscount();
    productDiscount.setProduct_id(entity.getProductId());
    productDiscount.setDiscount_type_id(entity.getDiscountTypeId());
    productDiscount.setDiscount_amount(entity.getDiscountAmount());
    productDiscount.setCurrency_id(entity.getCurrencyId());
    productDiscount.setStart_date(entity.getStartDate());
    productDiscount.setEnd_date(entity.getEndDate());
    productDiscountRepo.save(productDiscount);
  }

  public void updateProductDiscount(ProductDiscountRequestDTO dto) {
    ProductDiscountEntity entity = productDiscountMapper.toEntity(dto);
    ProductDiscount productDiscount = new ProductDiscount();
    productDiscount.setProduct_id(entity.getProductId());
    productDiscount.setDiscount_type_id(entity.getDiscountTypeId());
    productDiscount.setDiscount_amount(entity.getDiscountAmount());
    productDiscount.setCurrency_id(entity.getCurrencyId());
    productDiscount.setStart_date(entity.getStartDate());
    productDiscount.setEnd_date(entity.getEndDate());
    productDiscountRepo.save(productDiscount);
  }

  public String getProductNameByID(Long id) {
    return productDiscountRepo.getName(id);
  }

  public void deleteProductDiscount(Long id) {
    productDiscountRepo.deleteById(id);
  }

  public List<ProductDiscountResponseDTO> listProductDiscounts() {
    return productDiscountListRepo.findAllProductDiscounts().stream()
        .map(productDiscountMapper::toResponseDTO)
        .collect(Collectors.toList());
  }

  public ProductDiscountResponseDTO listProductDiscount(Long id) {
    return productDiscountMapper.toResponseDTO(productDiscountListRepo.findProductDiscountById(id));
  }

  public List<ProductDiscountResponseDTO> listProductDiscountsByUserID(Long user_id) {
    return productDiscountListRepo.findAllProductDiscountByUserID(user_id).stream()
        .map(productDiscountMapper::toResponseDTO)
        .collect(Collectors.toList());
  }

  public boolean checkDiscountDateValidity(ProductDiscountRequestDTO dto) {
    Date current_date = new Date();
    current_date = convertDateToStartOfTheDay(current_date);
    Date start_date = dto.getStartDate();
    Date end_date = dto.getEndDate();
    if (current_date.after(start_date))
      return false;
    if (current_date.after(end_date))
      return false;
    if (start_date.after(end_date))
      return false;
    return true;
  }
}
