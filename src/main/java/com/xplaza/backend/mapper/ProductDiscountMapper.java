/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.xplaza.backend.dao.ProductDiscountDAO;
import com.xplaza.backend.dto.ProductDiscountRequestDTO;
import com.xplaza.backend.dto.ProductDiscountResponseDTO;
import com.xplaza.backend.entity.ProductDiscountEntity;
import com.xplaza.backend.model.ProductDiscountList;

@Mapper(componentModel = "spring")
public interface ProductDiscountMapper {
  ProductDiscountMapper INSTANCE = Mappers.getMapper(ProductDiscountMapper.class);

  ProductDiscountEntity toEntity(ProductDiscountRequestDTO dto);

  ProductDiscountDAO toDAO(ProductDiscountEntity entity);

  ProductDiscountResponseDTO toResponseDTO(ProductDiscountDAO dao);

  ProductDiscountDAO toDAOFromResponse(ProductDiscountResponseDTO dto);

  ProductDiscountRequestDTO toRequestDTO(ProductDiscountEntity entity);

  ProductDiscountEntity toEntityFromDAO(ProductDiscountDAO dao);

  ProductDiscountResponseDTO toResponseDTO(ProductDiscountList list);
}
