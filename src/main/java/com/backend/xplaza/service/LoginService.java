package com.backend.xplaza.service;

import com.backend.xplaza.model.AdminUser;
import com.backend.xplaza.model.Login;
import com.backend.xplaza.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class LoginService {
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private LoginRepository loginRepo;

    public boolean isVaidUser (String username, String password) throws IOException {
        AdminUser adminUser = adminUserService.listAdminUser(username);
        String strOrgSalt = adminUser.getSalt();
        byte[] byteSalt = securityService.fromHex(strOrgSalt);
        byte[] loginPassword = securityService.getSaltedHashSHA512(password,byteSalt);
        byte[] storedPassword = securityService.fromHex(adminUser.getPassword());
        boolean result = Arrays.equals(loginPassword, storedPassword);
        return result;
    }

    public Login getAdminUserDetails(String username) {
        return loginRepo.findUserByUsername(username);
    }
}
