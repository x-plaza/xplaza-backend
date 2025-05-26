/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.model.AdminLogin;
import com.xplaza.backend.model.AdminUser;
import com.xplaza.backend.repository.LoginRepository;

@Service
public class LoginService {
  @Autowired
  private AdminUserService adminUserService;
  @Autowired
  private SecurityService securityService;
  @Autowired
  private LoginRepository loginRepo;

  public Boolean isVaidUser(String username, String password) throws IOException {
    AdminUser adminUser = adminUserService.listAdminUser(username);
    String strOrgSalt = adminUser.getSalt();
    byte[] byteSalt = securityService.fromHex(strOrgSalt);
    byte[] loginPassword = securityService.getSaltedHashSHA512(password, byteSalt);
    byte[] storedPassword = securityService.fromHex(adminUser.getPassword());
    boolean result = Arrays.equals(loginPassword, storedPassword);
    return result;
  }

  public AdminLogin getAdminUserDetails(String username) {
    return loginRepo.findUserByUsername(username);
  }

  public Boolean isVaidMasterAdmin(String username, String password) {
    AdminUser adminUser = adminUserService.listAdminUser(username);
    if (adminUser.getPassword().equals(password))
      return true;
    /*
     * String strOrgSalt = adminUser.getSalt(); byte[] byteSalt =
     * securityService.fromHex(strOrgSalt); byte[] loginPassword =
     * securityService.getSaltedHashSHA512(password,byteSalt); byte[] storedPassword
     * = securityService.fromHex(adminUser.getPassword()); boolean result =
     * Arrays.equals(loginPassword, storedPassword);
     */
    return false;
  }
}
