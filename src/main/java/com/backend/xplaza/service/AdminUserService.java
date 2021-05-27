package com.backend.xplaza.service;

import com.backend.xplaza.model.AdminUser;
import com.backend.xplaza.model.AdminUserList;
import com.backend.xplaza.repository.AdminUserListRepository;
import com.backend.xplaza.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {
    @Autowired
    private AdminUserRepository adminUserRepo;
    @Autowired
    private AdminUserListRepository adminUserListRepo;

    public void addAdminUser(AdminUser adminUser) {
        adminUserRepo.save(adminUser);
    }

    public void updateAdminUser(AdminUser adminUser) {
        adminUserRepo.save(adminUser);
    }

    public String getAdminUserNameByID(Long id) {
        return adminUserRepo.getName(id);
    }

    public void deleteAdminUser(Long id) {
        adminUserRepo.deleteById(id);
    }

    public List<AdminUserList> listAdminUsers() {
        return adminUserListRepo.findAllUsers();
    }

    public AdminUserList listAdminUser(Long id) {
        return adminUserListRepo.findUserById(id);
    }

    public AdminUser listAdminUser(String username) {
        return adminUserRepo.findUserByUsername(username);
    }
}
