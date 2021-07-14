package com.backend.xplaza.service;

import com.backend.xplaza.model.AdminUser;
import com.backend.xplaza.model.AdminUserList;
import com.backend.xplaza.model.AdminUserShopLink;
import com.backend.xplaza.repository.AdminUserListRepository;
import com.backend.xplaza.repository.AdminUserRepository;
import com.backend.xplaza.repository.AdminUserShopLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminUserService {
    @Autowired
    private AdminUserRepository adminUserRepo;
    @Autowired
    private AdminUserListRepository adminUserListRepo;
    @Autowired
    private AdminUserShopLinkRepository adminUserShopLinkRepo;

    @Transactional
    public void addAdminUser(AdminUser adminUser) {
        adminUserRepo.save(adminUser);
        for (AdminUserShopLink ausl : adminUser.getAdminUserShopLinks()) {
            adminUserShopLinkRepo.insert(adminUser.getId(),ausl.getShop_id());
        }
    }

    @Transactional
    public void updateAdminUser(AdminUser adminUser) {
        adminUserRepo.update(adminUser.getPassword(),adminUser.getSalt(),adminUser.getRole_id(),adminUser.getFull_name(),adminUser.getId());
        adminUserShopLinkRepo.deleteByAdminUserID(adminUser.getId());
        for (AdminUserShopLink ausl : adminUser.getAdminUserShopLinks()) {
            adminUserShopLinkRepo.insert(adminUser.getId(),ausl.getShop_id());
        }
    }

    public String getAdminUserNameByID(long id) {
        return adminUserRepo.getName(id);
    }

    @Transactional
    public void deleteAdminUser(long id) {
        adminUserShopLinkRepo.deleteByAdminUserID(id);
        adminUserRepo.deleteById(id);
    }

    public List<AdminUserList> listAdminUsers() {
        return adminUserListRepo.findAllUsers();
    }

    public AdminUserList listAdminUser(long id) {
        return adminUserListRepo.findUserById(id);
    }

    public AdminUser listAdminUser(String username) {
        return adminUserRepo.findUserByUsername(username);
    }
}
