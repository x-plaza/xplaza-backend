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

  Optional<Referral> findFirstByRefereeIdAndStatus(Long refereeId, Referral.ReferralStatus status);
}
