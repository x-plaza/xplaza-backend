/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.inventory.domain.entity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

/**
 * Stock reservation for orders.
 */
@Entity
@Table(name = "stock_reservations", indexes = {
    @Index(name = "idx_reservation_inventory", columnList = "inventory_id"),
    @Index(name = "idx_reservation_order", columnList = "order_id"),
    @Index(name = "idx_reservation_status", columnList = "status"),
    @Index(name = "idx_reservation_expires", columnList = "expires_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockReservation {

  @Id
  @Column(name = "reservation_id")
  @Builder.Default
  private UUID reservationId = UUID.randomUUID();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "inventory_id", nullable = false)
  private InventoryItem inventoryItem;

  @Column(name = "order_id")
  private UUID orderId;

  @Column(name = "cart_id")
  private UUID cartId;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  @Builder.Default
  private ReservationStatus status = ReservationStatus.RESERVED;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 20)
  @Builder.Default
  private ReservationType type = ReservationType.CART;

  @Column(name = "expires_at")
  private Instant expiresAt;

  @Column(name = "reserved_at")
  @Builder.Default
  private Instant reservedAt = Instant.now();

  @Column(name = "fulfilled_at")
  private Instant fulfilledAt;

  @Column(name = "released_at")
  private Instant releasedAt;

  @Column(name = "created_at")
  @Builder.Default
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at")
  @Builder.Default
  private Instant updatedAt = Instant.now();

  public enum ReservationStatus {
    RESERVED,
    FULFILLED,
    RELEASED,
    EXPIRED
  }

  public enum ReservationType {
    CART,
    ORDER,
    BACKORDER
  }

  @PrePersist
  protected void onCreate() {
    if (expiresAt == null) {
      // Default expiration based on type
      if (type == ReservationType.CART) {
        expiresAt = Instant.now().plus(30, ChronoUnit.MINUTES);
      } else {
        expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
      }
    }
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  public boolean isExpired() {
    return expiresAt != null && Instant.now().isAfter(expiresAt);
  }

  public void fulfill() {
    this.status = ReservationStatus.FULFILLED;
    this.fulfilledAt = Instant.now();
  }

  public void release() {
    this.status = ReservationStatus.RELEASED;
    this.releasedAt = Instant.now();
  }

  public void expire() {
    this.status = ReservationStatus.EXPIRED;
    this.releasedAt = Instant.now();
  }

  public void convertToOrder(UUID orderId) {
    this.orderId = orderId;
    this.type = ReservationType.ORDER;
    this.expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
  }
}
