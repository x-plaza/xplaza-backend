/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.marketing.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.common.events.DomainEvents;
import com.xplaza.backend.loyalty.service.LoyaltyService;
import com.xplaza.backend.marketing.domain.entity.Referral;
import com.xplaza.backend.marketing.domain.repository.ReferralRepository;

/**
 * End-to-end referral program. The {@code referrer} invites someone via
 * {@link #createReferral(Long, String)}; the system mints a unique short code
 * and (in a future iteration) emails a deep link to the referee. When the
 * referee registers, {@link #markAccepted(String, Long)} ties the customer to
 * the referral; their first order then triggers {@link #onOrderPlaced} which
 * credits the referrer with loyalty points and a small store credit.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReferralService {

  private static final SecureRandom RNG = new SecureRandom();
  private static final String ALPHABET = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";

  private final ReferralRepository referralRepository;
  private final LoyaltyService loyaltyService;

  @Value("${referral.reward-amount:10.00}")
  private BigDecimal rewardAmount;

  @Value("${referral.reward-points:500}")
  private long rewardPoints;

  @Transactional
  public Referral createReferral(Long referrerId, String refereeEmail) {
    var existing = referralRepository
        .findByRefereeEmailIgnoreCaseAndStatus(refereeEmail, Referral.ReferralStatus.PENDING);
    if (existing.isPresent()) {
      return existing.get();
    }
    var ref = Referral.builder()
        .referrerId(referrerId)
        .refereeEmail(refereeEmail.toLowerCase())
        .code(generateCode())
        .status(Referral.ReferralStatus.PENDING)
        .build();
    return referralRepository.save(ref);
  }

  @Transactional(readOnly = true)
  public List<Referral> listReferrals(Long referrerId) {
    return referralRepository.findByReferrerId(referrerId);
  }

  @Transactional
  public void markAccepted(String code, Long refereeCustomerId) {
    var ref = referralRepository.findByCode(code)
        .orElseThrow(() -> new IllegalArgumentException("Unknown referral code: " + code));
    if (ref.getStatus() != Referral.ReferralStatus.PENDING) {
      log.debug("Referral {} already in state {}; ignoring accept", code, ref.getStatus());
      return;
    }
    ref.setRefereeId(refereeCustomerId);
    ref.setStatus(Referral.ReferralStatus.ACCEPTED);
    referralRepository.save(ref);
  }

  /**
   * The referee's first order triggers the reward. We rely on the transactional
   * outbox (inside {@link LoyaltyService#grantPoints}) so the credit is durable
   * even if the listener crashes before settlement.
   */
  @Async
  @EventListener
  @Transactional
  public void onOrderPlaced(DomainEvents.OrderPlaced event) {
    if (event.customerId() == null) {
      return;
    }
    referralRepository
        .findFirstByRefereeIdAndStatus(event.customerId(), Referral.ReferralStatus.ACCEPTED)
        .ifPresent(ref -> reward(ref, event));
  }

  private void reward(Referral ref, DomainEvents.OrderPlaced event) {
    try {
      // Single source of truth for the grant: LoyaltyService persists the
      // points and publishes LoyaltyPointsEarned. We do NOT publish our own
      // event on top — that would double-count the reward in the outbox.
      loyaltyService.grantPoints(ref.getReferrerId(), rewardPoints, "referral:" + ref.getCode());
    } catch (Exception e) {
      log.error("Failed to credit referral loyalty points for referrer {} (order {}): {}",
          ref.getReferrerId(), event.orderId(), e.toString(), e);
      // Do not mark the referral as REWARDED if we could not credit the
      // points; the scheduler-driven retry path (future iteration) will pick
      // up ACCEPTED referrals and try again.
      return;
    }
    ref.setStatus(Referral.ReferralStatus.REWARDED);
    ref.setRewardedAt(Instant.now());
    ref.setRewardAmount(rewardAmount);
    referralRepository.save(ref);
  }

  private String generateCode() {
    var sb = new StringBuilder(8);
    for (int i = 0; i < 8; i++) {
      sb.append(ALPHABET.charAt(RNG.nextInt(ALPHABET.length())));
    }
    return sb.toString();
  }
}
