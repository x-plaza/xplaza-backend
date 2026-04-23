/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.customer.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

/**
 * An item in a customer's wishlist.
 */
@Entity
@Table(name = "wishlist_items", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "wishlist_id", "product_id", "variant_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItem {

  @Id
  @Column(name = "wishlist_item_id")
  @Builder.Default
  private UUID wishlistItemId = UUID.randomUUID();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wishlist_id", nullable = false)
  private Wishlist wishlist;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "variant_id")
  private UUID variantId;

  @Column(name = "note", columnDefinition = "TEXT")
  private String note;

  @Column(name = "price_at_add", precision = 15, scale = 2)
  private BigDecimal priceAtAdd;

  @Column(name = "notify_price_drop")
  @Builder.Default
  private Boolean notifyPriceDrop = false;

  @Column(name = "notify_back_in_stock")
  @Builder.Default
  private Boolean notifyBackInStock = false;

  @Column(name = "added_at")
  @Builder.Default
  private Instant addedAt = Instant.now();

  public UUID getWishlistId() {
    return wishlist != null ? wishlist.getWishlistId() : null;
  }
}
