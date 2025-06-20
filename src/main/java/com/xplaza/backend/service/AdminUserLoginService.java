/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.jpa.repository.LoginRepository;
import com.xplaza.backend.mapper.LoginMapper;
import com.xplaza.backend.service.entity.AdminUser;
import com.xplaza.backend.service.entity.Login;

@Service
@Transactional
public class AdminUserLoginService {
  private final AdminUserService adminUserService;
  private final SecurityService securityService;
  private final LoginRepository loginRepo;
  private final LoginMapper loginMapper;

  @Autowired
  public AdminUserLoginService(AdminUserService adminUserService, SecurityService securityService,
      LoginRepository loginRepo, LoginMapper loginMapper) {
    this.adminUserService = adminUserService;
    this.securityService = securityService;
    this.loginRepo = loginRepo;
    this.loginMapper = loginMapper;
  }

  public boolean isValidAdminUser(String username, String password) {
    AdminUser adminUser = adminUserService.listAdminUser(username);
    if (adminUser == null) {
      return false;
    }
    String strOrgSalt = adminUser.getSalt();
    byte[] byteSalt = securityService.fromHex(strOrgSalt);
    byte[] loginPassword = securityService.getSaltedHashSHA512(password, byteSalt);
    byte[] storedPassword = securityService.fromHex(adminUser.getPassword());
    return Arrays.equals(loginPassword, storedPassword);
  }

  public Login getAdminUserDetails(String username) {
    return loginMapper.toEntityFromDao(loginRepo.findUserByUsername(username));
  }

  public boolean isValidMasterAdmin(String username, String password) {
    AdminUser adminUser = adminUserService.listAdminUser(username);
    if (adminUser == null) {
      return false;
    }
    String strOrgSalt = adminUser.getSalt();
    byte[] byteSalt = securityService.fromHex(strOrgSalt);
    byte[] loginPassword = securityService.getSaltedHashSHA512(password, byteSalt);
    byte[] storedPassword = securityService.fromHex(adminUser.getPassword());
    return Arrays.equals(loginPassword, storedPassword);
  }
}
