/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.marketing.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.marketing.domain.entity.Referral;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {
  Optional<Referral> findByCode(String code);

  List<Referral> findByReferrerId(Long referrerId);

  Optional<Referral> findByRefereeEmailIgnoreCaseAndStatus(String email, Referral.ReferralStatus status);

  /**
   * Indexed lookup for the {@code OrderPlaced} listener. An order placed by
   * {@code refereeId} triggers the referrer reward, so this query runs on every
   * order and must be an indexed hit (idx_referrals_referee) — a
   * {@code findAll()} scan here would degrade to O(N) on the referrals table as
   * the program grows.
   */
  Optional<Referral> findFirstByRefereeIdAndStatus(Long refereeId, Referral.ReferralStatus status);
}
