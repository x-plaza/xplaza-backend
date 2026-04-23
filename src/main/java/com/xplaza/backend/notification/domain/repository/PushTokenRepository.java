/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.notification.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.notification.domain.entity.PushToken;

@Repository
public interface PushTokenRepository extends JpaRepository<PushToken, Long> {
  Optional<PushToken> findByToken(String token);

  List<PushToken> findByCustomerId(Long customerId);

  void deleteByToken(String token);
}
