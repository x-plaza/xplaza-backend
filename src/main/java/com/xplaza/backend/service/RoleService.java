/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.repository.RoleDAORepository;
import com.xplaza.backend.mapper.RoleMapper;
import com.xplaza.backend.service.entity.RoleEntity;

@Service
public class RoleService {
  @Autowired
  private RoleDAORepository roleDAORepo;
  @Autowired
  private RoleMapper roleMapper;

  public void addRole(RoleEntity entity) {
    RoleDAO dao = roleMapper.toDAO(entity);
    roleDAORepo.save(dao);
  }

  public void updateRole(RoleEntity entity) {
    RoleDAO dao = roleMapper.toDAO(entity);
    roleDAORepo.save(dao);
  }

  public List<RoleEntity> listRoles() {
    return roleDAORepo.findAll().stream().map(roleMapper::toEntityFromDAO).collect(Collectors.toList());
  }

  public void deleteRole(Long id) {
    roleDAORepo.deleteById(id);
  }

  public RoleEntity listRole(Long id) {
    return roleDAORepo.findById(id).map(roleMapper::toEntityFromDAO).orElse(null);
  }

  public String getRoleNameByID(Long id) {
    // Implement if needed in DAO repo
    return null;
  }

  public String getRoleNameByUserID(Long id) {
    // Implement if needed in DAO repo
    return null;
  }
}
