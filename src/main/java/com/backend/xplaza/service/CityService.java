package com.backend.xplaza.service;

import com.backend.xplaza.model.City;
import com.backend.xplaza.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepo;

    public void addCity(City city) {
        cityRepo.save(city);
    }

    public void updateCity(City city) {
        cityRepo.save(city);
    }

    public String getCityNameByID(Long id) {
        return cityRepo.getName(id);
    }

    public void deleteCity(Long id) {
        cityRepo.deleteById(id);
    }

    public List<City> listCities() {
        return cityRepo.findAll();
    }
}
