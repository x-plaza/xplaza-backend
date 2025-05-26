/*
 * Copyright (c) 2025 Xplaza or Xplaza affiliate company. All rights reserved.
 * Author: Mahiuddin Al Kamal <mahiuddinalkamal>
 */
package com.xplaza.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xplaza.backend.model.AdminUserList;
import com.xplaza.backend.model.Shop;
import com.xplaza.backend.model.ShopList;
import com.xplaza.backend.repository.AdminUserListRepository;
import com.xplaza.backend.repository.AdminUserShopLinkRepository;
import com.xplaza.backend.repository.ShopListRepository;
import com.xplaza.backend.repository.ShopRepository;

@Service
public class ShopService {
  @Autowired
  private ShopRepository shopRepo;
  @Autowired
  private ShopListRepository shopListRepo;
  @Autowired
  private AdminUserListRepository adminUserListRepo;
  @Autowired
  private AdminUserShopLinkRepository adminUserShopLinkRepo;

  @Transactional
  public void addShop(Shop shop) {
    Shop updatedShop = shopRepo.save(shop);
    List<AdminUserList> adminUserList = adminUserListRepo.findAllAdminUsersByRoleName("Master Admin");
    for (AdminUserList adminUser : adminUserList) {
      adminUserShopLinkRepo.insert(adminUser.getId(), updatedShop.getId());
    }
  }

  public void updateShop(Shop shop) {
    shopRepo.save(shop);
  }

  public List<ShopList> listShops() {
    return shopListRepo.findAllShopList();
  }

  public String getShopNameByID(Long id) {
    return shopRepo.getName(id);
  }

  public void deleteShop(Long id) {
    shopRepo.deleteById(id);
  }

  public ShopList listShop(Long id) {
    return shopListRepo.findShopListById(id);
  }

  public List<ShopList> listShopsByUserID(Long user_id) {
    return shopListRepo.findAllShopListByUserID(user_id);
  }

  public List<ShopList> listShopByLocation(Long id) {
    return shopListRepo.findShopListByLocationId(id);
  }
}
