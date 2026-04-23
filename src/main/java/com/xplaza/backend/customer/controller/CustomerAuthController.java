/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.customer.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.auth.dto.request.AuthenticationRequest;
import com.xplaza.backend.auth.dto.response.AuthenticationResponse;
import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.customer.domain.entity.Customer;
import com.xplaza.backend.customer.dto.CustomerRequest;
import com.xplaza.backend.customer.dto.response.CustomerProfileResponse;
import com.xplaza.backend.customer.service.CustomerService;

@RestController
@RequestMapping("/api/v1/customer/auth")
@RequiredArgsConstructor
public class CustomerAuthController {

  private final CustomerService customerService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
      @Valid @RequestBody CustomerRequest request) {
    return ResponseEntity.ok(ApiResponse.ok(customerService.register(request)));
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
      @Valid @RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(ApiResponse.ok(customerService.login(request)));
  }

  @PostMapping("/verify-email")
  public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam @NotBlank String token) {
    customerService.verifyEmail(token);
    return ResponseEntity.ok(ApiResponse.ok("Email verified"));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
    customerService.requestPasswordReset(request.email());
    return ResponseEntity.ok(ApiResponse.ok("If the email exists, a reset link has been sent"));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
    customerService.resetPassword(request.token(), request.newPassword());
    return ResponseEntity.ok(ApiResponse.ok("Password updated"));
  }

  @PostMapping("/mfa/enroll")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<ApiResponse<MfaEnrollResponse>> enrollMfa(@AuthenticationPrincipal Customer customer) {
    var qr = customerService.startMfaEnrollment(customer);
    return ResponseEntity.ok(ApiResponse.ok(new MfaEnrollResponse(qr)));
  }

  @PostMapping("/mfa/confirm")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<ApiResponse<Void>> confirmMfa(@AuthenticationPrincipal Customer customer,
      @RequestBody @Valid MfaConfirmRequest request) {
    boolean ok = customerService.confirmMfaEnrollment(customer, request.code());
    if (!ok) {
      return ResponseEntity.badRequest().body(ApiResponse.error("MFA_INVALID", "Invalid MFA code"));
    }
    return ResponseEntity.ok(ApiResponse.ok("MFA enabled"));
  }

  @PostMapping("/mfa/disable")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<ApiResponse<Void>> disableMfa(@AuthenticationPrincipal Customer customer) {
    customerService.disableMfa(customer);
    return ResponseEntity.ok(ApiResponse.ok("MFA disabled"));
  }

  @GetMapping("/me")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<ApiResponse<CustomerProfileResponse>> me(@AuthenticationPrincipal Customer customer) {
    return ResponseEntity.ok(ApiResponse.ok(CustomerProfileResponse.from(customer)));
  }

  // ---------- Request DTOs ----------
  public record ForgotPasswordRequest(@NotBlank @Email String email) {
  }

  public record ResetPasswordRequest(
      @NotBlank String token,
      @NotBlank String newPassword
  ) {
  }

  public record MfaConfirmRequest(@NotBlank String code) {
  }

  public record MfaEnrollResponse(String qrCodeImageDataUri) {
  }
}
