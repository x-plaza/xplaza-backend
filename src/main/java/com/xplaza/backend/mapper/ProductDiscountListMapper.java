/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.mapper;

import org.mapstruct.Mapper;

import com.xplaza.backend.jpa.dao.ProductDiscountListDao;
import com.xplaza.backend.service.entity.ProductDiscountList;

@Mapper(componentModel = "spring")
public interface ProductDiscountListMapper {
  ProductDiscountList toEntity(ProductDiscountListDao dao);

  ProductDiscountListDao toDao(ProductDiscountList entity);
}
