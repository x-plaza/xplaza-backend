/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Marker interface for transactionally-consistent domain events.
 *
 * <p>Implementations are immutable records that carry the minimal payload the
 * downstream listener needs. Producers publish via
 * {@link DomainEventPublisher#publish(DomainEvent)} which writes to the
 * transactional outbox in the same JDBC transaction; an asynchronous worker
 * dispatches the events to in-process listeners or external sinks.
 */
public sealed interface DomainEvent permits
    DomainEvents.OrderPlaced,
    DomainEvents.OrderCancelled,
    DomainEvents.OrderShipped,
    DomainEvents.PaymentSucceeded,
    DomainEvents.PaymentFailed,
    DomainEvents.RefundIssued,
    DomainEvents.CustomerRegistered,
    DomainEvents.CustomerEmailVerified,
    DomainEvents.PasswordResetRequested,
    DomainEvents.LoyaltyPointsEarned,
    DomainEvents.LoyaltyPointsRedeemed,
    DomainEvents.CartAbandoned,
    DomainEvents.InventoryReserved,
    DomainEvents.InventoryReleased,
    DomainEvents.ProductPriceChanged,
    DomainEvents.ProductIndexed,
    DomainEvents.ProductIndexInvalidated,
    DomainEvents.ReviewCreated,
    DomainEvents.NotificationRequested,
    DomainEvents.GiftCardIssued,
    DomainEvents.SubscriptionCreated,
    DomainEvents.VendorPayoutProcessed {

  UUID eventId();

  Instant occurredAt();

  default String type() {
    return getClass().getSimpleName();
  }
}
