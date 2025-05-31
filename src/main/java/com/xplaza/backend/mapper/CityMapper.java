/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.mapper;

import org.mapstruct.Mapper;

import com.xplaza.backend.dao.CityDAO;
import com.xplaza.backend.dto.*;
import com.xplaza.backend.entity.*;

@Mapper(componentModel = "spring")
public interface CityMapper {
  CityEntity toEntity(CityRequestDTO dto);

  CityDAO toDAO(CityEntity entity);

  CityResponseDTO toResponseDTO(CityDAO dao);

  CityDAO toDAOFromResponse(CityResponseDTO dto);

  CityRequestDTO toRequestDTO(CityEntity entity);

  CityEntity toEntityFromDAO(CityDAO dao);
}
