/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.mapper;

import org.mapstruct.Mapper;

import com.xplaza.backend.dao.*;
import com.xplaza.backend.dto.*;
import com.xplaza.backend.entity.*;

@Mapper(componentModel = "spring")
public interface StateMapper {
  StateEntity toEntity(StateRequestDTO dto);

  StateDAO toDAO(StateEntity entity);

  StateResponseDTO toResponseDTO(StateDAO dao);

  StateDAO toDAOFromResponse(StateResponseDTO dto);

  StateRequestDTO toRequestDTO(StateEntity entity);

  StateEntity toEntityFromDAO(StateDAO dao);
}
