/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.backend.xplaza.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.backend.xplaza.model.Currency;
import com.backend.xplaza.repository.CurrencyRepository;

@Service
public class CurrencyService {
  @Autowired
  private CurrencyRepository currencyRepo;

  public void addCurrency(Currency currency) {
    currencyRepo.save(currency);
  }

  public void updateCurrency(Currency currency) {
    currencyRepo.save(currency);
  }

  public List<Currency> listCurrencies() {
    return currencyRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
  }

  public String getCurrencyNameByID(Long id) {
    return currencyRepo.getName(id);
  }

  public void deleteCurrency(Long id) {
    currencyRepo.deleteById(id);
  }

  public Currency listCurrency(Long id) {
    return currencyRepo.findCurrencyById(id);
  }
}
