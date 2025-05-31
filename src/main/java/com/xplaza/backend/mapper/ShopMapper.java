/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.mapper;

import org.mapstruct.Mapper;

import com.xplaza.backend.dao.ShopDAO;
import com.xplaza.backend.dto.ShopRequestDTO;
import com.xplaza.backend.dto.ShopResponseDTO;
import com.xplaza.backend.entity.ShopEntity;

@Mapper(componentModel = "spring")
public interface ShopMapper {
  ShopEntity toEntity(ShopRequestDTO dto);

  ShopDAO toDAO(ShopEntity entity);

  ShopResponseDTO toResponseDTO(ShopDAO dao);

  ShopDAO toDAOFromResponse(ShopResponseDTO dto);

  ShopRequestDTO toRequestDTO(ShopEntity entity);

  ShopEntity toEntityFromDAO(ShopDAO dao);
}
