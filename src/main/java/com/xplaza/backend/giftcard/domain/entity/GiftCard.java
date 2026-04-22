/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.giftcard.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "gift_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCard {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "gift_card_id") private Long giftCardId;

  @Column(name = "code", unique = true, nullable = false, length = 32) private String code;
  @Column(name = "initial_balance", precision = 15, scale = 2, nullable = false) private BigDecimal initialBalance;
  @Column(name = "current_balance", precision = 15, scale = 2, nullable = false) private BigDecimal currentBalance;
  @Column(name = "currency", length = 3, nullable = false) private String currency;
  @Column(name = "issued_to_email") private String issuedToEmail;
  @Column(name = "issued_to_customer_id") private Long issuedToCustomerId;
  @Column(name = "expires_on") private LocalDate expiresOn;
  @Enumerated(EnumType.STRING) @Column(name = "status", length = 20, nullable = false)
  @Builder.Default private GiftCardStatus status = GiftCardStatus.ACTIVE;
  @Column(name = "created_at") @Builder.Default private Instant createdAt = Instant.now();

  public enum GiftCardStatus { ACTIVE, REDEEMED, EXPIRED, CANCELLED }
}
