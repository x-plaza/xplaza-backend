/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.giftcard.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.giftcard.domain.entity.GiftCard;

@Repository
public interface GiftCardRepository extends JpaRepository<GiftCard, Long> {
  Optional<GiftCard> findByCode(String code);
}
