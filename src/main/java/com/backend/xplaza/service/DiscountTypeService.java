package com.backend.xplaza.service;

import com.backend.xplaza.model.DiscountType;
import com.backend.xplaza.repository.DiscountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountTypeService {
    @Autowired
    private DiscountTypeRepository discountTypeRepo;

    public List<DiscountType> listDiscountTypes() {
        return discountTypeRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }
}
