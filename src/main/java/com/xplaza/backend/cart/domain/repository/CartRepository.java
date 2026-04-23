/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cart.domain.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.cart.domain.entity.Cart;
import com.xplaza.backend.cart.domain.entity.Cart.CartStatus;

/**
 * Repository for Cart aggregate root.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

  Optional<Cart> findByCustomerIdAndStatus(Long customerId, CartStatus status);

  default Optional<Cart> findActiveCartByCustomerId(Long customerId) {
    return findByCustomerIdAndStatus(customerId, CartStatus.ACTIVE);
  }

  Optional<Cart> findBySessionIdAndStatus(String sessionId, CartStatus status);

  default Optional<Cart> findActiveCartBySessionId(String sessionId) {
    return findBySessionIdAndStatus(sessionId, CartStatus.ACTIVE);
  }

  @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items WHERE c.id = :cartId")
  Optional<Cart> findByIdWithItems(@Param("cartId") UUID cartId);

  @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items WHERE c.customerId = :customerId AND c.status = 'ACTIVE'")
  Optional<Cart> findActiveCartByCustomerIdWithItems(@Param("customerId") Long customerId);

  @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.items WHERE c.sessionId = :sessionId AND c.status = 'ACTIVE'")
  Optional<Cart> findActiveCartBySessionIdWithItems(@Param("sessionId") String sessionId);

  List<Cart> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

  @Query("SELECT c FROM Cart c WHERE c.status = 'ACTIVE' AND c.expiresAt < :now")
  List<Cart> findAbandonedCarts(@Param("now") Instant now);

  @Query("SELECT c FROM Cart c WHERE c.status = 'ACTIVE' AND c.lastActivityAt < :since")
  List<Cart> findInactiveCarts(@Param("since") Instant since);

  long countByCustomerIdAndStatus(Long customerId, CartStatus status);

  @Modifying
  @Query("UPDATE Cart c SET c.status = 'ABANDONED' WHERE c.status = 'ACTIVE' AND c.expiresAt < :now")
  int markAbandonedCarts(@Param("now") Instant now);

  @Modifying
  @Query("DELETE FROM Cart c WHERE c.status = 'ABANDONED' AND c.updatedAt < :before")
  int deleteOldAbandonedCarts(@Param("before") Instant before);
}
