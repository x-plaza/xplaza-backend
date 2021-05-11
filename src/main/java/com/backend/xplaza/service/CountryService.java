package com.backend.xplaza.service;

import com.backend.xplaza.model.Country;
import com.backend.xplaza.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepo;

    public void addCountry(Country country) {
        countryRepo.save(country);
    }

    public void updateCountry(Country country) {
        countryRepo.save(country);
    }

    public String getCountryNameByID(Long id) {
        return countryRepo.getName(id);
    }

    public void deleteCountry(Long id) {
        countryRepo.deleteById(id);
    }

    public List<Country> listCountries() {
        return countryRepo.findAll();
    }

    public Country listCountry(long id) {
        return countryRepo.findItemById(id);
    }
}
