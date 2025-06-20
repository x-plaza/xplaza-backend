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

import com.xplaza.backend.common.util.ValidationUtil;
import com.xplaza.backend.exception.ResourceNotFoundException;
import com.xplaza.backend.jpa.dao.ShopDao;
import com.xplaza.backend.jpa.repository.ShopRepository;
import com.xplaza.backend.mapper.ShopMapper;
import com.xplaza.backend.service.entity.Shop;

@Service
public class ShopService {
  private final ShopRepository shopRepo;
  private final ShopMapper shopMapper;

  @Autowired
  public ShopService(ShopRepository shopRepo, ShopMapper shopMapper) {
    this.shopRepo = shopRepo;
    this.shopMapper = shopMapper;
  }

  @Transactional
  public Shop addShop(Shop shop) {
    // Validate input
    ValidationUtil.validateNotNull(shop, "Shop");
    ValidationUtil.validateNotEmpty(shop.getShopName(), "Shop name");
    ValidationUtil.validateNotEmpty(shop.getShopAddress(), "Shop address");
    ValidationUtil.validateNotEmpty(shop.getShopOwner(), "Shop owner");
    ShopDao shopDao = shopMapper.toDao(shop);
    ShopDao savedShopDao = shopRepo.save(shopDao);
    return shopMapper.toEntityFromDao(savedShopDao);
  }

  @Transactional
  public Shop updateShop(Long id, Shop shop) {
    // Validate input
    ValidationUtil.validateId(id, "Shop ID");
    ValidationUtil.validateNotNull(shop, "Shop");
    ValidationUtil.validateNotEmpty(shop.getShopName(), "Shop name");
    ValidationUtil.validateNotEmpty(shop.getShopAddress(), "Shop address");
    ValidationUtil.validateNotEmpty(shop.getShopOwner(), "Shop owner");
    // Check if shop exists
    shopRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + id));
    shop.setShopId(id);
    ShopDao shopDao = shopMapper.toDao(shop);
    ShopDao updatedShopDao = shopRepo.save(shopDao);
    return shopMapper.toEntityFromDao(updatedShopDao);
  }

  @Transactional
  public void deleteShop(Long id) {
    ValidationUtil.validateId(id, "Shop ID");
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
        .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + id));
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
