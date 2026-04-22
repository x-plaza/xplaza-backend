/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.payment.domain.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Tokenised payment method belonging to a customer. The {@code gatewayToken} is
 * the gateway's customer/payment_method id (e.g. Stripe pm_***); we never store
 * raw card data ourselves, only the safe display fields PCI allows.
 */
@Entity
@Table(name = "customer_payment_methods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerPaymentMethod {

  @Id
  @GeneratedValue
  @Column(name = "payment_method_id")
  private UUID paymentMethodId;

  @Column(name = "customer_id", nullable = false)
  private Long customerId;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false, length = 20)
  private MethodType type;

  @Column(name = "is_default")
  @Builder.Default
  private Boolean isDefault = false;

  @Column(name = "nickname", length = 50)
  private String nickname;

  @Column(name = "last4", length = 4)
  private String last4;

  @Column(name = "brand", length = 20)
  private String brand;

  @Column(name = "expiry_month")
  private Integer expiryMonth;

  @Column(name = "expiry_year")
  private Integer expiryYear;

  @Column(name = "cardholder_name", length = 255)
  private String cardholderName;

  @Column(name = "gateway", length = 50)
  private String gateway;

  @Column(name = "gateway_token", length = 255)
  private String gatewayToken;

  @Column(name = "billing_address_id")
  private Long billingAddressId;

  @Column(name = "created_at")
  @Builder.Default
  private Instant createdAt = Instant.now();

  @Column(name = "updated_at")
  @Builder.Default
  private Instant updatedAt = Instant.now();

  public enum MethodType {
    CARD,
    PAYPAL,
    BANK_ACCOUNT,
    WALLET
  }
}
