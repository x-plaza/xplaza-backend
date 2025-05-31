/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.mapper;

import org.mapstruct.Mapper;

import com.xplaza.backend.dao.GenericDAO;
import com.xplaza.backend.dto.GenericRequestDTO;
import com.xplaza.backend.dto.GenericResponseDTO;
import com.xplaza.backend.entity.GenericEntity;

@Mapper(componentModel = "spring")
public interface GenericMapper {
  GenericEntity toEntity(GenericRequestDTO dto);

  GenericDAO toDAO(GenericEntity entity);

  GenericResponseDTO toResponseDTO(GenericDAO dao);

  GenericDAO toDAOFromResponse(GenericResponseDTO dto);

  GenericRequestDTO toRequestDTO(GenericEntity entity);

  GenericEntity toEntityFromDAO(GenericDAO dao);
}
