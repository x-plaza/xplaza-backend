/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.jpa.dao.CurrencyDao;
import com.xplaza.backend.jpa.repository.CurrencyRepository;
import com.xplaza.backend.mapper.CurrencyMapper;
import com.xplaza.backend.service.entity.Currency;

@Service
public class CurrencyService {

  @Autowired
  private CurrencyRepository currencyRepository;

  @Autowired
  private CurrencyMapper currencyMapper;

  @Transactional
  public Currency addCurrency(Currency currency) {
    CurrencyDao currencyDao = currencyMapper.toDao(currency);
    CurrencyDao savedCurrencyDao = currencyRepository.save(currencyDao);
    return currencyMapper.toEntityFromDao(savedCurrencyDao);
  }

  @Transactional
  public Currency updateCurrency(Long id, Currency currency) {
    CurrencyDao existingCurrencyDao = currencyRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Currency not found with id: " + id));

    CurrencyDao currencyDao = currencyMapper.toDao(currency);
    currencyDao.setCurrencyId(existingCurrencyDao.getCurrencyId());

    CurrencyDao updatedCurrencyDao = currencyRepository.save(currencyDao);
    return currencyMapper.toEntityFromDao(updatedCurrencyDao);
  }

  @Transactional
  public void deleteCurrency(Long id) {
    currencyRepository.deleteById(id);
  }

  public List<Currency> listCurrencies() {
    List<CurrencyDao> currencyDaos = currencyRepository.findAll();
    return currencyDaos.stream()
        .map(currencyMapper::toEntityFromDao)
        .toList();
  }

  public Currency listCurrency(Long id) {
    CurrencyDao currencyDao = currencyRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Currency not found with id: " + id));
    return currencyMapper.toEntityFromDao(currencyDao);
  }
}
