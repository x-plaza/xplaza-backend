/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.payment.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.exception.ResourceNotFoundException;
import com.xplaza.backend.payment.domain.entity.CustomerPaymentMethod;
import com.xplaza.backend.payment.domain.repository.CustomerPaymentMethodRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class SavedPaymentMethodService {

  private final CustomerPaymentMethodRepository repo;

  @Transactional(readOnly = true)
  public List<CustomerPaymentMethod> list(Long customerId) {
    return repo.findByCustomerId(customerId);
  }

  @Transactional
  public CustomerPaymentMethod vault(Long customerId, CustomerPaymentMethod input) {
    input.setPaymentMethodId(null);
    input.setCustomerId(customerId);
    input.setCreatedAt(Instant.now());
    input.setUpdatedAt(Instant.now());
    if (Boolean.TRUE.equals(input.getIsDefault())) {
      repo.findByCustomerIdAndIsDefaultTrue(customerId).ifPresent(prev -> {
        prev.setIsDefault(false);
        repo.save(prev);
      });
    }
    return repo.save(input);
  }

  @Transactional
  public void remove(Long customerId, UUID id) {
    var method = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));
    if (!method.getCustomerId().equals(customerId)) {
      throw new ResourceNotFoundException("Payment method not found");
    }
    repo.delete(method);
  }

  @Transactional
  public CustomerPaymentMethod setDefault(Long customerId, UUID id) {
    var method = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));
    if (!method.getCustomerId().equals(customerId)) {
      throw new ResourceNotFoundException("Payment method not found");
    }
    repo.findByCustomerIdAndIsDefaultTrue(customerId).ifPresent(prev -> {
      prev.setIsDefault(false);
      repo.save(prev);
    });
    method.setIsDefault(true);
    return repo.save(method);
  }
}
