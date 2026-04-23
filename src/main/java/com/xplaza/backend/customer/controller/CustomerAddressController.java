/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.customer.controller;

import java.time.Instant;
import java.util.List;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.xplaza.backend.common.util.ApiResponse;
import com.xplaza.backend.customer.domain.entity.Customer;
import com.xplaza.backend.customer.domain.entity.CustomerAddress;
import com.xplaza.backend.customer.domain.repository.CustomerAddressRepository;
import com.xplaza.backend.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/api/v1/customer/addresses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerAddressController {

  private final CustomerAddressRepository repo;

  @GetMapping
  public ResponseEntity<ApiResponse<List<CustomerAddress>>> list(@AuthenticationPrincipal Customer customer) {
    return ResponseEntity.ok(ApiResponse.ok(repo.findByCustomerIdAndIsActiveTrue(customer.getCustomerId())));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<CustomerAddress>> get(@AuthenticationPrincipal Customer customer,
      @PathVariable Long id) {
    var addr = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found"));
    if (!addr.getCustomerId().equals(customer.getCustomerId())) {
      throw new ResourceNotFoundException("Address not found");
    }
    return ResponseEntity.ok(ApiResponse.ok(addr));
  }

  @PostMapping
  @Transactional
  public ResponseEntity<ApiResponse<CustomerAddress>> create(@AuthenticationPrincipal Customer customer,
      @Valid @RequestBody CustomerAddress request) {
    request.setAddressId(null);
    request.setCustomerId(customer.getCustomerId());
    request.setIsActive(true);
    request.setCreatedAt(Instant.now());
    request.setUpdatedAt(Instant.now());
    if (Boolean.TRUE.equals(request.getIsDefault())) {
      repo.findByCustomerIdAndIsDefaultTrue(customer.getCustomerId()).ifPresent(prev -> {
        prev.setIsDefault(false);
        repo.save(prev);
      });
    }
    return ResponseEntity.ok(ApiResponse.ok(repo.save(request)));
  }

  @PutMapping("/{id}")
  @Transactional
  public ResponseEntity<ApiResponse<CustomerAddress>> update(@AuthenticationPrincipal Customer customer,
      @PathVariable Long id, @Valid @RequestBody CustomerAddress patch) {
    var existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found"));
    if (!existing.getCustomerId().equals(customer.getCustomerId())) {
      throw new ResourceNotFoundException("Address not found");
    }
    existing.setLabel(patch.getLabel());
    existing.setFirstName(patch.getFirstName());
    existing.setLastName(patch.getLastName());
    existing.setCompany(patch.getCompany());
    existing.setAddressLine1(patch.getAddressLine1());
    existing.setAddressLine2(patch.getAddressLine2());
    existing.setCity(patch.getCity());
    existing.setState(patch.getState());
    existing.setPostalCode(patch.getPostalCode());
    existing.setCountryCode(patch.getCountryCode());
    existing.setPhone(patch.getPhone());
    existing.setInstructions(patch.getInstructions());
    existing.setType(patch.getType() != null ? patch.getType() : existing.getType());
    existing.setUpdatedAt(Instant.now());
    return ResponseEntity.ok(ApiResponse.ok(repo.save(existing)));
  }

  @PostMapping("/{id}/default")
  @Transactional
  public ResponseEntity<ApiResponse<CustomerAddress>> setDefault(@AuthenticationPrincipal Customer customer,
      @PathVariable Long id) {
    var existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found"));
    if (!existing.getCustomerId().equals(customer.getCustomerId())) {
      throw new ResourceNotFoundException("Address not found");
    }
    repo.findByCustomerIdAndIsDefaultTrue(customer.getCustomerId()).ifPresent(prev -> {
      prev.setIsDefault(false);
      repo.save(prev);
    });
    existing.setIsDefault(true);
    return ResponseEntity.ok(ApiResponse.ok(repo.save(existing)));
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal Customer customer,
      @PathVariable Long id) {
    var existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found"));
    if (!existing.getCustomerId().equals(customer.getCustomerId())) {
      throw new ResourceNotFoundException("Address not found");
    }
    existing.setIsActive(false);
    repo.save(existing);
    return ResponseEntity.ok(ApiResponse.ok(null));
  }
}
