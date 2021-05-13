package com.backend.xplaza.service;

import com.backend.xplaza.model.Shop;
import com.backend.xplaza.model.ShopList;
import com.backend.xplaza.repository.ShopListRepository;
import com.backend.xplaza.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {
    @Autowired
    private ShopRepository shopRepo;
    @Autowired
    private ShopListRepository shopLocRepo;

    public void addShop(Shop shop) {
        shopRepo.save(shop);
    }

    public void updateShop(Shop shop) {
        shopRepo.save(shop);
    }

    public List<ShopList> listShops() {
        //return shopRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return shopLocRepo.findAllItem();
    }

    public String getShopNameByID(Long id) {
        return shopRepo.getName(id);
    }

    public void deleteShop(Long id) {
        shopRepo.deleteById(id);
    }

    public ShopList listShop(long id) {
        return shopLocRepo.findItemById(id);
    }
}
