/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.xplaza.backend.http.dto.request.ProductStockRequest;
import com.xplaza.backend.http.dto.response.ProductStockResponse;
import com.xplaza.backend.service.entity.ProductStock;

@Mapper(componentModel = "spring")
public interface ProductStockMapper {
  @Mapping(target = "id", source = "id")
  @Mapping(target = "productId", source = "productId")
  @Mapping(target = "productName", source = "productName")
  @Mapping(target = "quantity", source = "quantity")
  @Mapping(target = "shopId", source = "shopId")
  ProductStock toEntity(ProductStockRequest request);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "productId", source = "productId")
  @Mapping(target = "productName", source = "productName")
  @Mapping(target = "quantity", source = "quantity")
  @Mapping(target = "shopId", source = "shopId")
  ProductStockResponse toResponse(ProductStock entity);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "productId", source = "productId")
  @Mapping(target = "productName", source = "productName")
  @Mapping(target = "quantity", source = "quantity")
  @Mapping(target = "shopId", source = "shopId")
  ProductStockDao toDao(ProductStock entity);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "productId", source = "productId")
  @Mapping(target = "productName", source = "productName")
  @Mapping(target = "quantity", source = "quantity")
  @Mapping(target = "shopId", source = "shopId")
  ProductStock toEntityFromDao(ProductStockDao dao);
}