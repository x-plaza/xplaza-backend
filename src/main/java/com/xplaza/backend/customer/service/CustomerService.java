/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.customer.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.auth.domain.entity.EmailVerificationToken;
import com.xplaza.backend.auth.domain.entity.PasswordResetToken;
import com.xplaza.backend.auth.domain.repository.EmailVerificationTokenRepository;
import com.xplaza.backend.auth.domain.repository.PasswordResetTokenRepository;
import com.xplaza.backend.auth.dto.request.AuthenticationRequest;
import com.xplaza.backend.auth.dto.response.AuthenticationResponse;
import com.xplaza.backend.auth.service.AccountLockoutPolicy;
import com.xplaza.backend.auth.service.MfaService;
import com.xplaza.backend.common.events.DomainEventPublisher;
import com.xplaza.backend.common.events.DomainEvents;
import com.xplaza.backend.common.util.JwtUtil;
import com.xplaza.backend.customer.domain.entity.Customer;
import com.xplaza.backend.customer.domain.repository.CustomerRepository;
import com.xplaza.backend.customer.dto.CustomerRequest;
import com.xplaza.backend.exception.ResourceAlreadyExistsException;
import com.xplaza.backend.exception.ResourceNotFoundException;
import com.xplaza.backend.notification.service.EmailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

  private static final SecureRandom RNG = new SecureRandom();
  private static final Duration EMAIL_TOKEN_TTL = Duration.ofHours(48);
  private static final Duration RESET_TOKEN_TTL = Duration.ofMinutes(30);

  private final CustomerRepository customerRepository;
  private final EmailVerificationTokenRepository emailVerificationTokens;
  private final PasswordResetTokenRepository passwordResetTokens;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final EmailService emailService;
  private final MfaService mfaService;
  private final DomainEventPublisher eventPublisher;

  // ---------- Registration ----------
  @Transactional
  public AuthenticationResponse register(CustomerRequest request) {
    if (customerRepository.existsByEmail(request.getEmail())) {
      throw new ResourceAlreadyExistsException("Email already exists: " + request.getEmail());
    }

    Customer customer = Customer.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .phoneNumber(request.getPhoneNumber())
        .role("CUSTOMER")
        .enabled(true)
        .emailVerified(false)
        .failedLoginAttempts(0)
        .build();

    customer = customerRepository.save(customer);

    issueEmailVerificationToken(customer);

    eventPublisher.publish(new DomainEvents.CustomerRegistered(
        UUID.randomUUID(), Instant.now(), customer.getCustomerId(), customer.getEmail()));

    String jwtToken = jwtUtil.generateJwtToken(customer);
    String refreshToken = jwtUtil.generateRefreshToken(customer);
    return new AuthenticationResponse(jwtToken, refreshToken);
  }

  // ---------- Login (with lockout + MFA) ----------
  @Transactional
  public AuthenticationResponse login(AuthenticationRequest request) {
    Customer customer = customerRepository.findByEmail(request.getUsername())
        .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

    if (Boolean.FALSE.equals(customer.getEnabled())) {
      throw new DisabledException("Account is disabled");
    }
    if (!customer.isAccountNonLocked()) {
      throw new LockedException("Account is locked. Try again later.");
    }
    if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
      registerFailedAttempt(customer);
      throw new BadCredentialsException("Invalid email or password");
    }

    customer.setFailedLoginAttempts(0);
    customer.setLockedUntil(null);
    customer.setLastLoginAt(LocalDateTime.now());
    customerRepository.save(customer);

    String jwtToken = jwtUtil.generateJwtToken(customer);
    String refreshToken = jwtUtil.generateRefreshToken(customer);
    return new AuthenticationResponse(jwtToken, refreshToken);
  }

  public boolean verifyMfa(Customer customer, String code) {
    return Boolean.TRUE.equals(customer.getMfaEnabled())
        && customer.getMfaSecret() != null
        && mfaService.verify(customer.getMfaSecret(), code);
  }

  private void registerFailedAttempt(Customer customer) {
    int attempts = Optional.ofNullable(customer.getFailedLoginAttempts()).orElse(0) + 1;
    customer.setFailedLoginAttempts(attempts);
    var until = AccountLockoutPolicy.computeLockedUntil(attempts);
    if (until != null) {
      customer.setLockedUntil(until);
    }
    customerRepository.save(customer);
  }

  // ---------- Email verification ----------
  @Transactional
  public void issueEmailVerificationToken(Customer customer) {
    var token = randomToken();
    var record = EmailVerificationToken.builder()
        .customerId(customer.getCustomerId())
        .token(token)
        .expiresAt(Instant.now().plus(EMAIL_TOKEN_TTL))
        .createdAt(Instant.now())
        .build();
    emailVerificationTokens.save(record);
    emailService.sendEmail(customer.getEmail(), "Verify your X-Plaza email",
        "Click to verify: https://app.xplaza.com/verify-email?token=" + token);
  }

  @Transactional
  public void verifyEmail(String token) {
    var record = emailVerificationTokens.findByToken(token)
        .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));
    if (!record.isValid()) {
      throw new IllegalStateException("Token is expired or already used");
    }
    record.setConsumedAt(Instant.now());
    emailVerificationTokens.save(record);

    if (record.getCustomerId() != null) {
      var customer = customerRepository.findById(record.getCustomerId())
          .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
      customer.setEmailVerified(true);
      customer.setEmailVerifiedAt(Instant.now());
      customerRepository.save(customer);
      eventPublisher.publish(new DomainEvents.CustomerEmailVerified(
          UUID.randomUUID(), Instant.now(), customer.getCustomerId()));
    }
  }

  // ---------- Password reset ----------
  @Transactional
  public void requestPasswordReset(String email) {
    var customerOpt = customerRepository.findByEmail(email);
    if (customerOpt.isEmpty()) {
      log.info("Password reset requested for unknown email: {}", email);
      return;
    }
    var customer = customerOpt.get();
    var token = randomToken();
    passwordResetTokens.save(PasswordResetToken.builder()
        .customerId(customer.getCustomerId())
        .token(token)
        .expiresAt(Instant.now().plus(RESET_TOKEN_TTL))
        .createdAt(Instant.now())
        .build());
    emailService.sendEmail(customer.getEmail(), "Reset your X-Plaza password",
        "Click to reset: https://app.xplaza.com/reset-password?token=" + token);
    eventPublisher.publish(new DomainEvents.PasswordResetRequested(
        UUID.randomUUID(), Instant.now(), customer.getCustomerId(), customer.getEmail(), token));
  }

  @Transactional
  public void resetPassword(String token, String newPassword) {
    var record = passwordResetTokens.findByToken(token)
        .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));
    if (!record.isValid()) {
      throw new IllegalStateException("Token is expired or already used");
    }
    var customer = customerRepository.findById(record.getCustomerId())
        .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    customer.setPassword(passwordEncoder.encode(newPassword));
    customer.setFailedLoginAttempts(0);
    customer.setLockedUntil(null);
    customerRepository.save(customer);
    record.setConsumedAt(Instant.now());
    passwordResetTokens.save(record);
  }

  // ---------- MFA enrolment ----------
  @Transactional
  public String startMfaEnrollment(Customer customer) {
    var secret = mfaService.generateSecret();
    customer.setMfaSecret(secret);
    customerRepository.save(customer);
    return mfaService.generateQrCodeImageDataUri(customer.getEmail(), secret);
  }

  @Transactional
  public boolean confirmMfaEnrollment(Customer customer, String code) {
    if (customer.getMfaSecret() == null) {
      return false;
    }
    if (!mfaService.verify(customer.getMfaSecret(), code)) {
      return false;
    }
    customer.setMfaEnabled(true);
    customerRepository.save(customer);
    return true;
  }

  @Transactional
  public void disableMfa(Customer customer) {
    customer.setMfaEnabled(false);
    customer.setMfaSecret(null);
    customerRepository.save(customer);
  }

  // ---------- GDPR: export & erase ----------
  @Transactional(readOnly = true)
  public Customer exportCustomerData(Long customerId) {
    return getCustomer(customerId);
  }

  @Transactional
  public void eraseCustomerData(Long customerId) {
    var customer = getCustomer(customerId);
    String marker = "deleted-" + customerId + "-" + UUID.randomUUID();
    customer.setEmail(marker + "@deleted.local");
    customer.setFirstName("Deleted");
    customer.setLastName("User");
    customer.setPhoneNumber(null);
    customer.setPassword("$2a$12$" + Base64.getUrlEncoder().withoutPadding()
        .encodeToString(randomBytes(40)).substring(0, 53));
    customer.setEnabled(false);
    customer.setMfaEnabled(false);
    customer.setMfaSecret(null);
    customer.setOauthProvider(null);
    customer.setOauthSubject(null);
    customer.setTaxId(null);
    customerRepository.save(customer);
  }

  // ---------- Profile ----------
  @Transactional(readOnly = true)
  public Customer getCustomer(Long id) {
    return customerRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
  }

  @Transactional
  public Customer updateProfile(Long id, CustomerRequest request) {
    Customer customer = getCustomer(id);
    customer.setFirstName(request.getFirstName());
    customer.setLastName(request.getLastName());
    customer.setPhoneNumber(request.getPhoneNumber());
    return customerRepository.save(customer);
  }

  // ---------- helpers ----------
  private static String randomToken() {
    byte[] buf = randomBytes(32);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
  }

  private static byte[] randomBytes(int n) {
    byte[] buf = new byte[n];
    RNG.nextBytes(buf);
    return buf;
  }
}
