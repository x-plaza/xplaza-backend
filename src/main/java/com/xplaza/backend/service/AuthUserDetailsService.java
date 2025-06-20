/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.repository.AdminUserRepository;
import com.xplaza.backend.mapper.AdminUserMapper;
import com.xplaza.backend.service.entity.AdminUser;

@Service
public class AuthUserDetailsService implements UserDetailsService {

  private final AdminUserRepository adminUserRepository;
  private final AdminUserMapper adminUserMapper;

  @Autowired
  public AuthUserDetailsService(AdminUserRepository adminUserRepository, AdminUserMapper adminUserMapper) {
    this.adminUserRepository = adminUserRepository;
    this.adminUserMapper = adminUserMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var adminUser = adminUserRepository.findUserByUsername(username);
    if (adminUser == null) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
    AdminUser entity = adminUserMapper.toEntityFromDao(adminUser);
    return new User(entity.getUserName(), entity.getPassword(), new ArrayList<>());
  }
}
