/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.jpa.dao.ShopDao;
import com.xplaza.backend.jpa.repository.ShopRepository;
import com.xplaza.backend.mapper.ShopMapper;
import com.xplaza.backend.service.entity.Shop;

@Service
public class ShopService {
  @Autowired
  private ShopRepository shopRepo;

  @Autowired
  private ShopMapper shopMapper;

  @Transactional
  public Shop addShop(Shop shop) {
    ShopDao shopDao = shopMapper.toDao(shop);
    ShopDao savedShopDao = shopRepo.save(shopDao);
    return shopMapper.toEntityFromDao(savedShopDao);
  }

  @Transactional
  public Shop updateShop(Long id, Shop shop) {
    ShopDao existingShopDao = shopRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Shop not found with id: " + id));

    shop.setShopId(id);
    ShopDao shopDao = shopMapper.toDao(shop);
    ShopDao updatedShopDao = shopRepo.save(shopDao);
    return shopMapper.toEntityFromDao(updatedShopDao);
  }

  @Transactional
  public void deleteShop(Long id) {
    shopRepo.deleteById(id);
  }

  public List<Shop> listShops() {
    List<ShopDao> shopDaos = shopRepo.findAll();
    return shopDaos.stream()
        .map(shopMapper::toEntityFromDao)
        .collect(Collectors.toList());
  }

  public Shop listShop(Long id) {
    ShopDao shopDao = shopRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Shop not found with id: " + id));
    return shopMapper.toEntityFromDao(shopDao);
  }

  public List<Shop> listShopsByLocation(Long locationId) {
    List<ShopDao> shopDaos = shopRepo.findByLocationId(locationId);
    return shopDaos.stream()
        .map(shopMapper::toEntityFromDao)
        .collect(Collectors.toList());
  }

  public List<Shop> listShopsByOwner(Long ownerId) {
    List<ShopDao> shopDaos = shopRepo.findByShopOwner(ownerId);
    return shopDaos.stream()
        .map(shopMapper::toEntityFromDao)
        .collect(Collectors.toList());
  }
}
