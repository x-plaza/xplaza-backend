/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.model.AdminUser;
import com.xplaza.backend.model.AdminUserList;
import com.xplaza.backend.model.AdminUserShopLink;
import com.xplaza.backend.model.PlatformInfo;
import com.xplaza.backend.repository.AdminUserListRepository;
import com.xplaza.backend.repository.AdminUserRepository;
import com.xplaza.backend.repository.AdminUserShopLinkRepository;

@Service
public class AdminUserService {
  @Autowired
  private AdminUserRepository adminUserRepo;
  @Autowired
  private AdminUserListRepository adminUserListRepo;
  @Autowired
  private AdminUserShopLinkRepository adminUserShopLinkRepo;
  @Autowired
  private EmailSenderService emailSenderService;
  @Autowired
  private PlatformInfoService platformInfoService;
  @Autowired
  private Environment env;

  @Transactional
  public void addAdminUser(AdminUser adminUser) {
    adminUserRepo.save(adminUser);
    for (AdminUserShopLink ausl : adminUser.getAdminUserShopLinks()) {
      adminUserShopLinkRepo.insert(adminUser.getId(), ausl.getShop_id());
    }
  }

  @Transactional
  public void updateAdminUser(AdminUser adminUser) {
    adminUserRepo.update(adminUser.getRole_id(), adminUser.getFull_name(), adminUser.getId());
    adminUserShopLinkRepo.deleteByAdminUserID(adminUser.getId());
    for (AdminUserShopLink ausl : adminUser.getAdminUserShopLinks()) {
      adminUserShopLinkRepo.insert(adminUser.getId(), ausl.getShop_id());
    }
  }

  @Transactional
  public void deleteAdminUser(Long id) {
    adminUserShopLinkRepo.deleteByAdminUserID(id);
    adminUserRepo.deleteById(id);
  }

  public String getAdminUserNameByID(Long id) {
    return adminUserRepo.getName(id);
  }

  public List<AdminUserList> listAdminUsers() {
    return adminUserListRepo.findAllUsers();
  }

  public List<AdminUserList> listAdminUsersByRoleName(String role_name) {
    return adminUserListRepo.findAllAdminUsersByRoleName(role_name);
  }

  public AdminUserList listAdminUser(Long id) {
    return adminUserListRepo.findUserById(id);
  }

  public AdminUser listAdminUser(String username) {
    return adminUserRepo.findUserByUsername(username);
  }

  public void changeAdminUserPassword(String new_password, String salt, String user_name) {
    adminUserRepo.changePassword(new_password, salt, user_name);
  }

  public void sendLoginDetails(String email, String temp_password) {
    // Get platform info
    PlatformInfo platformInfo = platformInfoService.listPlatform();
    // Send email
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(env.getProperty("spring.mail.username"));
    mailMessage.setTo(email);
    mailMessage.setSubject("Your " + platformInfo.getName() + ".com Admin Login Details!");
    mailMessage.setText("Congratulations! Your " + platformInfo.getName()
        + " Admin account has been created successfully.\n\n" +
        "Please use the following login details to login to the admin panel and please change the password immediately.\n\n"
        +
        "Email : " + email + "\n" + "Password : " + temp_password + "\n\n" +
        "With Regards,\n" + "Team " + platformInfo.getName());
    emailSenderService.sendEmail(mailMessage);
  }
}
