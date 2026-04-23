/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.giftcard.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "gift_card_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCardTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "transaction_id")
  private Long transactionId;

  @Column(name = "gift_card_id", nullable = false)
  private Long giftCardId;
  @Enumerated(EnumType.STRING)
  @Column(name = "type", length = 20, nullable = false)
  private TransactionType type;
  @Column(name = "amount", precision = 15, scale = 2, nullable = false)
  private BigDecimal amount;
  @Column(name = "balance_after", precision = 15, scale = 2, nullable = false)
  private BigDecimal balanceAfter;
  @Column(name = "order_id")
  private UUID orderId;
  @Column(name = "created_at")
  @Builder.Default
  private Instant createdAt = Instant.now();

  public enum TransactionType {
    ISSUE,
    REDEEM,
    REFUND,
    EXPIRE,
    ADJUST
  }
}
