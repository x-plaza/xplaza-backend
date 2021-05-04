package com.backend.xplaza.repository;

import com.backend.xplaza.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {

}
