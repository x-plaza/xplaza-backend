/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplaza.backend.jpa.dao.Shop;
import com.xplaza.backend.jpa.repository.ShopRepository;
import com.xplaza.backend.mapper.ShopMapper;
import com.xplaza.backend.service.entity.ShopEntity;

@Service
public class ShopService {
  @Autowired
  private ShopRepository shopRepo;
  @Autowired
  private ShopMapper shopMapper;

  public void addShop(ShopEntity shopEntity) {
    Shop dao = shopMapper.toDAO(shopEntity);
    shopRepo.save(dao);
  }

  public void updateShop(ShopEntity shopEntity) {
    Shop dao = shopMapper.toDAO(shopEntity);
    shopRepo.save(dao);
  }

  public void deleteShop(Long id) {
    shopRepo.deleteById(id);
  }

  public List<ShopEntity> listShops() {
    return shopRepo.findAll().stream().map(shopMapper::toEntityFromDAO).collect(Collectors.toList());
  }

  public ShopEntity listShop(Long id) {
    Shop dao = shopRepo.findById(id).orElse(null);
    return shopMapper.toEntityFromDAO(dao);
  }
}
