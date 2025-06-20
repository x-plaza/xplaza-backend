/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.repository.ConfirmationTokenRepository;
import com.xplaza.backend.mapper.ConfirmationTokenMapper;
import com.xplaza.backend.service.entity.ConfirmationToken;
import com.xplaza.backend.service.entity.PlatformInfo;

@Service
public class ConfirmationTokenService {
  private final ConfirmationTokenRepository confirmationTokenRepo;
  @Autowired
  private EmailSenderService emailSenderService;
  @Autowired
  private PlatformInfoService platformInfoService;
  @Autowired
  private ConfirmationTokenMapper confirmationTokenMapper;
  @Autowired
  private Environment env;

  @Autowired
  public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepo) {
    this.confirmationTokenRepo = confirmationTokenRepo;
  }

  public ConfirmationToken getConfirmationToken(String confirmationToken) {
    return confirmationTokenMapper.toEntityFromDao(confirmationTokenRepo.findByConfirmationToken(confirmationToken));
  }

  // Helper method to create and save a ConfirmationToken
  private ConfirmationToken createAndSaveToken(String email, String type) {
    ConfirmationToken confirmationToken = new ConfirmationToken(email, type);
    confirmationTokenRepo.save(confirmationTokenMapper.toDao(confirmationToken));
    return confirmationToken;
  }

  // Helper method to send an email
  private void sendEmail(String to, String subject, String text) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(env.getProperty("spring.mail.username"));
    mailMessage.setTo(to);
    mailMessage.setSubject(subject);
    mailMessage.setText(text);
    emailSenderService.sendEmail(mailMessage);
  }

  // Helper method to get platform info
  private PlatformInfo getPlatformInfo() {
    return platformInfoService.listPlatform();
  }

  public void sendConfirmationToken(String email) {
    ConfirmationToken confirmationToken = createAndSaveToken(email, "Code");
    PlatformInfo platformInfo = getPlatformInfo();
    String subject = "Complete " + platformInfo.getName() + ".com Sign Up!";
    String text = "To confirm your " + platformInfo.getName() + ".com Admin account, " +
        "please use the following Code:\n\n" + confirmationToken.getConfirmationToken() + "\n\n" +
        "With Regards,\n" + "Team " + platformInfo.getName();
    sendEmail(email, subject, text);
  }

  public void sendOTP(String email) {
    ConfirmationToken confirmationToken = createAndSaveToken(email, "OTP");
    PlatformInfo platformInfo = getPlatformInfo();
    String subject = "One Time Password!";
    String text = "To reset your " + platformInfo.getName() + ".com Admin account password, " +
        "please use the following OTP:\n\n" + confirmationToken.getConfirmationToken() + "\n\n" +
        "With Regards,\n" + "Team " + platformInfo.getName();
    sendEmail(email, subject, text);
  }

  public void sendConfirmationTokenToCustomer(String email) {
    ConfirmationToken confirmationToken = createAndSaveToken(email, "Code");
    PlatformInfo platformInfo = getPlatformInfo();
    String subject = "Complete " + platformInfo.getName() + ".com Sign Up!";
    String text = "To confirm your email, please use the following Code:\n\n"
        + confirmationToken.getConfirmationToken() + "\n\n" +
        "With Regards,\n" + "Team " + platformInfo.getName();
    sendEmail(email, subject, text);
  }

  public void sendOTPToCustomer(String email) {
    ConfirmationToken confirmationToken = createAndSaveToken(email, "OTP");
    PlatformInfo platformInfo = getPlatformInfo();
    String subject = "One Time Password!";
    String text = "To reset your " + platformInfo.getName() + ".com account password, " +
        "please use the following OTP:\n\n"
        + confirmationToken.getConfirmationToken() + "\n\n" +
        "With Regards,\n" + "Team " + platformInfo.getName();
    sendEmail(email, subject, text);
  }
}
