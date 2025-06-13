/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.mapper;

import org.mapstruct.Mapper;

import com.xplaza.backend.jpa.dao.DiscountDao;
import com.xplaza.backend.service.entity.Discount;

@Mapper(componentModel = "spring")
public interface DiscountListMapper {
  DiscountDao toDao(Discount entity);

  Discount toEntityFromDao(DiscountDao dao);
}