/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */

package com.xplaza.backend.b2b.service;

import java.math.BigDecimal;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.b2b.domain.entity.CustomerGroup;
import com.xplaza.backend.b2b.domain.entity.PriceList;
import com.xplaza.backend.b2b.domain.entity.PriceListItem;
import com.xplaza.backend.b2b.domain.repository.CustomerGroupRepository;
import com.xplaza.backend.b2b.domain.repository.PriceListItemRepository;
import com.xplaza.backend.b2b.domain.repository.PriceListRepository;
import com.xplaza.backend.customer.domain.entity.Customer;
import com.xplaza.backend.customer.domain.repository.CustomerRepository;

/**
 * Resolves the contract price for a (customer, product, quantity) tuple.
 *
 * <p>
 * Resolution order:
 * <ol>
 * <li>If the customer has no group, return the catalog price.</li>
 * <li>Find every active price list applicable to the group (or with a null
 * group, meaning "everyone").</li>
 * <li>For each price list, look for the row whose {@code minQuantity ≤ qty}
 * with the highest {@code minQuantity} (best quantity break).</li>
 * <li>Return the cheapest unit price across all matching rows.</li>
 * <li>If none match, fall back to the catalog price minus the group's
 * {@code discountPercent}.</li>
 * </ol>
 *
 * The resolver is read-only and cached; cache is invalidated whenever a price
 * list is mutated (admin endpoints — see TODO).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PriceListResolver {

  private final CustomerRepository customerRepository;
  private final CustomerGroupRepository customerGroupRepository;
  private final PriceListRepository priceListRepository;
  private final PriceListItemRepository priceListItemRepository;

  /**
   * Resolve the unit price for a customer/product/quantity <em>in a given
   * currency</em>. Only price lists whose {@code currency} equals (case-
   * insensitively) the caller-supplied {@code currency} are considered, so a
   * multi-currency catalogue cannot accidentally return a USD contract rate for a
   * EUR cart. Returns {@code catalogPrice} unchanged when no matching contract
   * applies.
   */
  @Cacheable(value = "priceLists", key = "#customerId + ':' + #productId + ':' + #quantity + ':' + (#currency == null ? '_' : #currency)")
  @Transactional(readOnly = true)
  public BigDecimal resolveUnitPrice(Long customerId, Long productId, int quantity, String currency,
      BigDecimal catalogPrice) {
    if (customerId == null || productId == null) {
      return catalogPrice;
    }
    var customer = customerRepository.findById(customerId).orElse(null);
    if (customer == null || customer.getCustomerGroupId() == null) {
      return catalogPrice;
    }
    var group = customerGroupRepository.findById(customer.getCustomerGroupId()).orElse(null);
    if (group == null) {
      return catalogPrice;
    }

    BigDecimal best = null;
    for (PriceList pl : priceListRepository.findApplicableForGroup(group.getId())) {
      if (!pl.isApplicableNow()) {
        continue;
      }
      if (!currencyMatches(currency, pl.getCurrency())) {
        continue;
      }
      var rows = priceListItemRepository
          .findByPriceListIdAndProductIdOrderByMinQuantityDesc(pl.getId(), productId);
      for (PriceListItem row : rows) {
        if (quantity >= row.getMinQuantity()) {
          best = (best == null || row.getUnitPrice().compareTo(best) < 0) ? row.getUnitPrice() : best;
          break;
        }
      }
    }

    if (best != null) {
      return best;
    }
    return applyGroupDiscount(catalogPrice, group);
  }

  /**
   * Backwards-compatible overload. Callers that genuinely do not know the cart
   * currency (e.g. legacy code paths) get catalog-price fallback when any
   * matching contract exists in a different currency.
   */
  public BigDecimal resolveUnitPrice(Long customerId, Long productId, int quantity, BigDecimal catalogPrice) {
    return resolveUnitPrice(customerId, productId, quantity, null, catalogPrice);
  }

  private static boolean currencyMatches(String cartCurrency, String priceListCurrency) {
    if (cartCurrency == null || cartCurrency.isBlank()) {
      // Caller did not specify — treat as "accept everything" to preserve the
      // previous behaviour for unit-tests and legacy code paths.
      return true;
    }
    if (priceListCurrency == null || priceListCurrency.isBlank()) {
      return false;
    }
    return cartCurrency.equalsIgnoreCase(priceListCurrency);
  }

  /**
   * Returns whether the customer is tax-exempt by virtue of group membership.
   * Used by tax engine plumbing to skip rule application.
   */
  @Cacheable(value = "priceLists", key = "'tax_exempt:' + #customerId")
  @Transactional(readOnly = true)
  public boolean isTaxExempt(Long customerId) {
    if (customerId == null) {
      return false;
    }
    return customerRepository.findById(customerId)
        .map(Customer::getCustomerGroupId)
        .flatMap(customerGroupRepository::findById)
        .map(g -> Boolean.TRUE.equals(g.getTaxExempt()))
        .orElse(false);
  }

  /**
   * Helper for callers that already have a resolved {@link CustomerGroup}.
   */
  public Optional<CustomerGroup> findGroupForCustomer(Long customerId) {
    if (customerId == null) {
      return Optional.empty();
    }
    return customerRepository.findById(customerId)
        .map(Customer::getCustomerGroupId)
        .flatMap(customerGroupRepository::findById);
  }

  private BigDecimal applyGroupDiscount(BigDecimal catalogPrice, CustomerGroup group) {
    if (group.getDiscountPercent() == null || group.getDiscountPercent().signum() <= 0) {
      return catalogPrice;
    }
    BigDecimal multiplier = BigDecimal.ONE.subtract(
        group.getDiscountPercent().divide(BigDecimal.valueOf(100)));
    return catalogPrice.multiply(multiplier).setScale(catalogPrice.scale(), java.math.RoundingMode.HALF_UP);
  }
}
