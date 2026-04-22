/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.giftcard.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.common.events.DomainEventPublisher;
import com.xplaza.backend.common.events.DomainEvents;
import com.xplaza.backend.giftcard.domain.entity.GiftCard;
import com.xplaza.backend.giftcard.domain.entity.GiftCardTransaction;
import com.xplaza.backend.giftcard.domain.repository.GiftCardRepository;
import com.xplaza.backend.giftcard.domain.repository.GiftCardTransactionRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class GiftCardService {

  private static final SecureRandom RNG = new SecureRandom();
  private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

  private final GiftCardRepository giftCardRepo;
  private final GiftCardTransactionRepository txnRepo;
  private final DomainEventPublisher eventPublisher;

  @Transactional
  public GiftCard issue(BigDecimal amount, String currency, String email, Long customerId, LocalDate expiresOn) {
    var card = GiftCard.builder()
        .code(generateCode())
        .initialBalance(amount)
        .currentBalance(amount)
        .currency(currency)
        .issuedToEmail(email)
        .issuedToCustomerId(customerId)
        .expiresOn(expiresOn)
        .status(GiftCard.GiftCardStatus.ACTIVE)
        .createdAt(Instant.now())
        .build();
    card = giftCardRepo.save(card);
    txnRepo.save(GiftCardTransaction.builder()
        .giftCardId(card.getGiftCardId())
        .type(GiftCardTransaction.TransactionType.ISSUE)
        .amount(amount)
        .balanceAfter(amount)
        .build());
    eventPublisher.publish(new DomainEvents.GiftCardIssued(
        UUID.randomUUID(), Instant.now(), card.getGiftCardId(), amount, card.getCode()));
    return card;
  }

  @Transactional
  public GiftCard redeem(String code, BigDecimal amount, UUID orderId) {
    var card = giftCardRepo.findByCode(code).orElseThrow(() -> new IllegalArgumentException("invalid_code"));
    if (card.getStatus() != GiftCard.GiftCardStatus.ACTIVE) {
      throw new IllegalArgumentException("card_not_active");
    }
    if (card.getExpiresOn() != null && card.getExpiresOn().isBefore(LocalDate.now())) {
      card.setStatus(GiftCard.GiftCardStatus.EXPIRED);
      giftCardRepo.save(card);
      throw new IllegalArgumentException("card_expired");
    }
    if (card.getCurrentBalance().compareTo(amount) < 0) {
      throw new IllegalArgumentException("insufficient_balance");
    }
    var newBalance = card.getCurrentBalance().subtract(amount);
    card.setCurrentBalance(newBalance);
    if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
      card.setStatus(GiftCard.GiftCardStatus.REDEEMED);
    }
    giftCardRepo.save(card);
    txnRepo.save(GiftCardTransaction.builder()
        .giftCardId(card.getGiftCardId())
        .type(GiftCardTransaction.TransactionType.REDEEM)
        .amount(amount.negate())
        .balanceAfter(newBalance)
        .orderId(orderId)
        .build());
    return card;
  }

  @Transactional(readOnly = true)
  public GiftCard checkBalance(String code) {
    return giftCardRepo.findByCode(code).orElseThrow(() -> new IllegalArgumentException("invalid_code"));
  }

  static String generateCode() {
    var sb = new StringBuilder(16);
    for (int i = 0; i < 16; i++) {
      if (i > 0 && i % 4 == 0)
        sb.append('-');
      sb.append(ALPHABET.charAt(RNG.nextInt(ALPHABET.length())));
    }
    return sb.toString();
  }
}
