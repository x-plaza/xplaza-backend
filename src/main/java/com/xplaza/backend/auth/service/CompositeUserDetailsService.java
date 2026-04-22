/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.auth.service;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Resolves a {@link UserDetails} from either the admin or customer table. The
 * role embedded in the JWT (claim {@code role}) tells us which lookup to try
 * first, but we fall back to the other repository so a customer JWT cannot
 * accidentally resolve to a stale admin entry and vice versa.
 */
@Primary
@Service
public class CompositeUserDetailsService implements UserDetailsService {

  private final AuthUserDetailsService adminService;
  private final CustomerUserDetailsService customerService;

  public CompositeUserDetailsService(AuthUserDetailsService adminService,
      CustomerUserDetailsService customerService) {
    this.adminService = adminService;
    this.customerService = customerService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return loadUserByUsernameWithRole(username, null);
  }

  /**
   * Like {@link #loadUserByUsername(String)} but biased by the role hint to avoid
   * an unnecessary first lookup against the wrong table.
   */
  public UserDetails loadUserByUsernameWithRole(String username, String roleHint) throws UsernameNotFoundException {
    if ("CUSTOMER".equalsIgnoreCase(roleHint)) {
      return customerService.loadUserByUsername(username);
    }
    if ("ADMIN".equalsIgnoreCase(roleHint)) {
      return adminService.loadUserByUsername(username);
    }
    try {
      return adminService.loadUserByUsername(username);
    } catch (UsernameNotFoundException ignored) {
      return customerService.loadUserByUsername(username);
    }
  }
}
