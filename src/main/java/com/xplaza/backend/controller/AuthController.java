/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xplaza.backend.common.util.JwtUtil;
import com.xplaza.backend.model.AuthenticationRequest;
import com.xplaza.backend.model.AuthenticationResponse;
import com.xplaza.backend.service.AuthUserDetailsService;

@RestController
@RequestMapping("/api/v1/authenticate")
public class AuthController {
  @Autowired
  private JwtUtil jwtTokenUtil;

  @Autowired
  private AuthUserDetailsService authUserDetailsService;

  @PostMapping
  public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
      @RequestBody AuthenticationRequest authenticationRequest) {
    UserDetails userDetails = authUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    String jwtToken = jwtTokenUtil.generateJwtToken(userDetails);
    String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
    return ResponseEntity.ok(new AuthenticationResponse(jwtToken, refreshToken));
  }
}
