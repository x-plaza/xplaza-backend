/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.notification.service;

import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.xplaza.backend.common.util.LogSanitizer;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;

  @Async
  @CircuitBreaker(name = "mail")
  @Retry(name = "mail")
  public void sendEmail(String to, String subject, String text) {
    sendTemplate(to, subject, "email-template",
        Map.of("title", subject, "message", text));
  }

  @Async
  @CircuitBreaker(name = "mail")
  @Retry(name = "mail")
  public void sendTemplate(String to, String subject, String template, Map<String, Object> model) {
    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

      Context context = new Context();
      if (model != null) {
        model.forEach(context::setVariable);
      }
      String htmlContent = templateEngine.process(template, context);

      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(htmlContent, true);
      helper.setFrom("noreply@xplaza.com");

      mailSender.send(mimeMessage);
      log.info("HTML Email '{}' (template={}) sent to {}",
          LogSanitizer.forLog(subject), LogSanitizer.forLog(template), LogSanitizer.forLog(to));
    } catch (MessagingException e) {
      log.error("Failed to send HTML email to {}", LogSanitizer.forLog(to), e);
    }
  }
}
