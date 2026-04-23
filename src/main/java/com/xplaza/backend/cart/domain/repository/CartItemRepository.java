/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.cart.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.cart.domain.entity.CartItem;
import com.xplaza.backend.cart.domain.entity.CartItem.ItemStatus;

/**
 * Repository for CartItem entities.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

  List<CartItem> findByCartId(UUID cartId);

  List<CartItem> findByCartIdAndStatus(UUID cartId, ItemStatus status);

  @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.productId = :productId AND ci.variantId = :variantId")
  CartItem findByCartIdAndProductAndVariant(@Param("cartId") UUID cartId, @Param("productId") Long productId,
      @Param("variantId") UUID variantId);

  @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.productId = :productId AND ci.variantId IS NULL")
  CartItem findByCartIdAndProductWithoutVariant(@Param("cartId") UUID cartId, @Param("productId") Long productId);

  @Query("SELECT COUNT(ci) FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.status = 'ACTIVE'")
  long countActiveItemsInCart(@Param("cartId") UUID cartId);

  @Query("SELECT COALESCE(SUM(ci.quantity), 0) FROM CartItem ci WHERE ci.cart.id = :cartId AND ci.status = 'ACTIVE'")
  int getTotalQuantityInCart(@Param("cartId") UUID cartId);

  @Modifying
  @Query("DELETE FROM CartItem ci WHERE ci.cart.id = :cartId")
  void deleteByCartId(@Param("cartId") UUID cartId);

  @Modifying
  @Query("UPDATE CartItem ci SET ci.status = :status WHERE ci.id = :itemId")
  void updateStatus(@Param("itemId") UUID itemId, @Param("status") ItemStatus status);

  default List<CartItem> findOutOfStockItems(UUID cartId) {
    return findByCartIdAndStatus(cartId, CartItem.ItemStatus.OUT_OF_STOCK);
  }

  List<CartItem> findByProductId(Long productId);

  List<CartItem> findByVariantId(UUID variantId);
}
