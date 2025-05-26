/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.xplaza.backend.model.Role;
import com.xplaza.backend.repository.RoleRepository;

@Service
public class RoleService {
  @Autowired
  private RoleRepository roleRepo;

  public void addRole(Role role) {
    roleRepo.save(role);
  }

  public void updateRole(Role role) {
    roleRepo.save(role);
  }

  public List<Role> listRoles() {
    return roleRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
  }

  public String getRoleNameByID(Long id) {
    return roleRepo.getName(id);
  }

  public void deleteRole(Long id) {
    roleRepo.deleteById(id);
  }

  public Role listRole(Long id) {
    return roleRepo.findRoleById(id);
  }

  public String getRoleNameByUserID(Long id) {
    return roleRepo.getRoleNameByUserID(id);
  }
}
