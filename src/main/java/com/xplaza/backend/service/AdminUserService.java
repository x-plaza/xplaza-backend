/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.jpa.dao.AdminUserDao;
import com.xplaza.backend.jpa.repository.AdminUserRepository;
import com.xplaza.backend.mapper.AdminUserMapper;
import com.xplaza.backend.service.entity.AdminUser;
import com.xplaza.backend.service.entity.PlatformInfo;

@Service
@Transactional
public class AdminUserService {
  @Autowired
  private AdminUserRepository adminUserRepository;
  @Autowired
  private AdminUserMapper adminUserMapper;
  @Autowired
  private EmailSenderService emailSenderService;
  @Autowired
  private PlatformInfoService platformInfoService;
  @Autowired
  private Environment env;

  public void addAdminUser(AdminUser entity) {
    AdminUserDao dao = adminUserMapper.toDao(entity);
    adminUserRepository.save(dao);
  }

  public void updateAdminUser(AdminUser entity) {
    AdminUserDao dao = adminUserMapper.toDao(entity);
    adminUserRepository.save(dao);
    // handle shop links if needed
  }

  public List<AdminUser> listAdminUsers() {
    return adminUserRepository.findAll().stream().map(adminUserMapper::toEntityFromDao).collect(Collectors.toList());
  }

  public AdminUser listAdminUser(Long id) {
    return adminUserRepository.findById(id).map(adminUserMapper::toEntityFromDao).orElse(null);
  }

  public AdminUser listAdminUser(String username) {
    AdminUserDao dao = adminUserRepository.findUserByUsername(username);
    return dao != null ? adminUserMapper.toEntityFromDao(dao) : null;
  }

  public void deleteAdminUser(Long id) {
    adminUserRepository.deleteById(id);
  }

  public void sendLoginDetails(String email, String tempPassword) {
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
        "Email : " + email + "\n" + "Password : " + tempPassword + "\n\n" +
        "With Regards,\n" + "Team " + platformInfo.getName());
    emailSenderService.sendEmail(mailMessage);
  }

  public void changeAdminUserPassword(String password, String salt, String username) {
    adminUserRepository.changePassword(password, salt, username);
  }

  public String getAdminUserNameByID(Long id) {
    return adminUserRepository.getName(id);
  }
}
