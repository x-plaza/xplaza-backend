/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.backend.xplaza.common.util.JwtUtil;
import com.backend.xplaza.model.AuthenticationRequest;
import com.backend.xplaza.model.AuthenticationResponse;
import com.backend.xplaza.service.AuthUserDetailsService;

@Controller
@RequestMapping("/authenticate")
public class AuthController {
  @Autowired
  private JwtUtil jwtTokenUtil;

  @Autowired
  private AuthUserDetailsService authUserDetailsService;

  @PostMapping
  public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
      @RequestBody AuthenticationRequest authenticationRequest) {
    UserDetails userDetails = authUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    String jwt = jwtTokenUtil.generateJwtToken(userDetails);
    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }
}
