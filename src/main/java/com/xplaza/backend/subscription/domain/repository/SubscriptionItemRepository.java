/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.subscription.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.subscription.domain.entity.SubscriptionItem;

@Repository
public interface SubscriptionItemRepository extends JpaRepository<SubscriptionItem, Long> {
  List<SubscriptionItem> findBySubscriptionId(Long subscriptionId);
}
