package com.backend.xplaza.service;

import com.backend.xplaza.model.Currency;
import com.backend.xplaza.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public String getCurrencyNameByID(long id) {
        return currencyRepo.getName(id);
    }

    public void deleteCurrency(long id) {
        currencyRepo.deleteById(id);
    }

    public Currency listCurrency(long id) {
        return currencyRepo.findCurrencyById(id);
    }
}
