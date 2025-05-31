/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.mapper;

import org.mapstruct.Mapper;

import com.xplaza.backend.dao.ModuleDAO;
import com.xplaza.backend.dto.ModuleRequestDTO;
import com.xplaza.backend.dto.ModuleResponseDTO;
import com.xplaza.backend.entity.ModuleEntity;

@Mapper(componentModel = "spring")
public interface ModuleMapper {
  ModuleEntity toEntity(ModuleRequestDTO dto);

  ModuleDAO toDAO(ModuleEntity entity);

  ModuleResponseDTO toResponseDTO(ModuleDAO dao);

  ModuleDAO toDAOFromResponse(ModuleResponseDTO dto);

  ModuleRequestDTO toRequestDTO(ModuleEntity entity);

  ModuleEntity toEntityFromDAO(ModuleDAO dao);
}
