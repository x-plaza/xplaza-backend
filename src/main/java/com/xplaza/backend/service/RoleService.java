/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.dao.RoleDao;
import com.xplaza.backend.jpa.repository.RoleRepository;
import com.xplaza.backend.mapper.RoleMapper;
import com.xplaza.backend.service.entity.Role;

@Service
public class RoleService {
  @Autowired
  private RoleRepository roleRepo;
  @Autowired
  private RoleMapper roleMapper;

  public void addRole(Role entity) {
    RoleDao dao = roleMapper.toDao(entity);
    roleRepo.save(dao);
  }

  public void updateRole(Role entity) {
    RoleDao dao = roleMapper.toDao(entity);
    roleRepo.save(dao);
  }

  public List<Role> listRoles() {
    return roleRepo.findAll().stream().map(roleMapper::toEntityFromDao).collect(Collectors.toList());
  }

  public void deleteRole(Long id) {
    roleRepo.deleteById(id);
  }

  public Role listRole(Long id) {
    return roleRepo.findById(id)
        .map(roleMapper::toEntityFromDao).orElse(null);
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
