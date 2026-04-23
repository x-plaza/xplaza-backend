/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.tax.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.tax.domain.entity.TaxRule;

@Repository
public interface TaxRuleRepository extends JpaRepository<TaxRule, Long> {
  List<TaxRule> findByTaxZoneIdAndActiveTrueOrderByPriorityAsc(Long taxZoneId);
}
