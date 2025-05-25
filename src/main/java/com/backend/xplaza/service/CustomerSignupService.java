/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.backend.xplaza.model.CustomerDetails;
import com.backend.xplaza.model.PlatformInfo;
import com.backend.xplaza.repository.CustomerSignupRepository;

@Service
public class CustomerSignupService {
  @Autowired
  private CustomerSignupRepository customerSignupRepo;
  @Autowired
  private EmailSenderService emailSenderService;
  @Autowired
  private PlatformInfoService platformInfoService;
  @Autowired
  private Environment env;

  public void signupCustomer(CustomerDetails customer) {
    customerSignupRepo.save(customer);
  }

  public void sendLoginDetails(String email, String password) {
    // Get platform info
    PlatformInfo platformInfo = platformInfoService.listPlatform();
    // Send email
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(env.getProperty("spring.mail.username"));
    mailMessage.setTo(email);
    mailMessage.setSubject("Your " + platformInfo.getName() + ".com Login Details!");
    mailMessage.setText(
        "Congratulations! Your " + platformInfo.getName() + ".com Customer account has been created successfully.\n\n" +
            "Please use the following login details to login to the " + platformInfo.getName() + " account.\n\n" +
            "Email : " + email + "\n" + "Password : " + password + "\n\n" +
            "With Regards,\n" + "Team " + platformInfo.getName());
    emailSenderService.sendEmail(mailMessage);
  }

}
