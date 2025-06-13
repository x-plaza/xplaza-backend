/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.mapper;

import org.mapstruct.Mapper;

import com.xplaza.backend.jpa.dao.OrderItemDao;
import com.xplaza.backend.service.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemListMapper {
  OrderItemDao toDao(OrderItem entity);

  OrderItem toEntityFromDao(OrderItemDao dao);
}