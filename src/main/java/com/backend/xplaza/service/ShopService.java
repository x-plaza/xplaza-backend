package com.backend.xplaza.service;

import com.backend.xplaza.model.AdminUserList;
import com.backend.xplaza.model.Shop;
import com.backend.xplaza.model.ShopList;
import com.backend.xplaza.repository.AdminUserListRepository;
import com.backend.xplaza.repository.AdminUserShopLinkRepository;
import com.backend.xplaza.repository.ShopListRepository;
import com.backend.xplaza.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            adminUserShopLinkRepo.insert(adminUser.getId(),updatedShop.getId());
        }
    }

    public void updateShop(Shop shop) {
        shopRepo.save(shop);
    }

    public List<ShopList> listShops() { return shopListRepo.findAllShopList(); }

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
