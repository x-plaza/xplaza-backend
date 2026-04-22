/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.payment.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xplaza.backend.payment.domain.entity.CustomerPaymentMethod;

@Repository
public interface CustomerPaymentMethodRepository extends JpaRepository<CustomerPaymentMethod, UUID> {
  List<CustomerPaymentMethod> findByCustomerId(Long customerId);
  Optional<CustomerPaymentMethod> findByCustomerIdAndIsDefaultTrue(Long customerId);
}
