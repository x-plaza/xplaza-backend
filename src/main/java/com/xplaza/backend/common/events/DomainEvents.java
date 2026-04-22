/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.common.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Container for the project's sealed domain event records. Keeping the records
 * grouped here makes the {@link DomainEvent} permits clause readable and lets
 * us add new events without touching every producer/consumer in the codebase.
 */
public final class DomainEvents {
  private DomainEvents() {
  }

  public record OrderPlaced(
      UUID eventId,
      Instant occurredAt,
      UUID orderId,
      Long customerId,
      Long shopId,
      BigDecimal total,
      String currency
  ) implements DomainEvent {
  }

  public record OrderCancelled(
      UUID eventId,
      Instant occurredAt,
      UUID orderId,
      String reason
  ) implements DomainEvent {
  }

  public record OrderShipped(
      UUID eventId,
      Instant occurredAt,
      UUID orderId,
      Long shipmentId,
      String trackingNumber
  ) implements DomainEvent {
  }

  public record PaymentSucceeded(
      UUID eventId,
      Instant occurredAt,
      UUID orderId,
      Long paymentId,
      BigDecimal amount,
      String currency,
      String gateway
  ) implements DomainEvent {
  }

  public record PaymentFailed(
      UUID eventId,
      Instant occurredAt,
      UUID orderId,
      String reason,
      String gateway
  ) implements DomainEvent {
  }

  public record RefundIssued(
      UUID eventId,
      Instant occurredAt,
      Long refundId,
      UUID orderId,
      BigDecimal amount,
      String reason
  ) implements DomainEvent {
  }

  public record CustomerRegistered(
      UUID eventId,
      Instant occurredAt,
      Long customerId,
      String email
  ) implements DomainEvent {
  }

  public record CustomerEmailVerified(
      UUID eventId,
      Instant occurredAt,
      Long customerId
  ) implements DomainEvent {
  }

  public record PasswordResetRequested(
      UUID eventId,
      Instant occurredAt,
      Long customerId,
      String email,
      String token
  ) implements DomainEvent {
  }

  public record LoyaltyPointsEarned(
      UUID eventId,
      Instant occurredAt,
      Long customerId,
      long points,
      String reason
  ) implements DomainEvent {
  }

  public record LoyaltyPointsRedeemed(
      UUID eventId,
      Instant occurredAt,
      Long customerId,
      long points,
      UUID orderId
  ) implements DomainEvent {
  }

  public record CartAbandoned(
      UUID eventId,
      Instant occurredAt,
      Long cartId,
      Long customerId,
      String email
  ) implements DomainEvent {
  }

  public record InventoryReserved(
      UUID eventId,
      Instant occurredAt,
      Long productId,
      Long variantId,
      int quantity,
      UUID orderId
  ) implements DomainEvent {
  }

  public record InventoryReleased(
      UUID eventId,
      Instant occurredAt,
      Long productId,
      Long variantId,
      int quantity,
      UUID orderId
  ) implements DomainEvent {
  }

  public record ProductPriceChanged(
      UUID eventId,
      Instant occurredAt,
      Long productId,
      BigDecimal oldPrice,
      BigDecimal newPrice
  ) implements DomainEvent {
  }

  public record ProductIndexed(
      UUID eventId,
      Instant occurredAt,
      Long productId
  ) implements DomainEvent {
  }

  public record ProductIndexInvalidated(
      UUID eventId,
      Instant occurredAt,
      Long productId
  ) implements DomainEvent {
  }

  public record ReviewCreated(
      UUID eventId,
      Instant occurredAt,
      Long reviewId,
      Long productId,
      Long customerId,
      int rating
  ) implements DomainEvent {
  }

  public record NotificationRequested(
      UUID eventId,
      Instant occurredAt,
      Long customerId,
      String channel,
      String templateCode,
      String payload
  ) implements DomainEvent {
  }

  public record GiftCardIssued(
      UUID eventId,
      Instant occurredAt,
      Long giftCardId,
      BigDecimal amount,
      String code
  ) implements DomainEvent {
  }

  public record SubscriptionCreated(
      UUID eventId,
      Instant occurredAt,
      Long subscriptionId,
      Long customerId
  ) implements DomainEvent {
  }

  public record VendorPayoutProcessed(
      UUID eventId,
      Instant occurredAt,
      Long payoutId,
      Long shopId,
      BigDecimal amount
  ) implements DomainEvent {
  }
}
