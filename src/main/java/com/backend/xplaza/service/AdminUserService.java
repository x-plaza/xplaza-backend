package com.backend.xplaza.service;

import com.backend.xplaza.model.AdminUser;
import com.backend.xplaza.model.AdminUserList;
import com.backend.xplaza.model.AdminUserShopLink;
import com.backend.xplaza.repository.AdminUserListRepository;
import com.backend.xplaza.repository.AdminUserRepository;
import com.backend.xplaza.repository.AdminUserShopLinkRepository;
import com.backend.xplaza.repository.ConfirmationTokenRepository;
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
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepo;
    //@Autowired
    //private EmailSenderService emailSenderService;

    @Transactional
    public void addAdminUser(AdminUser adminUser) {
        adminUserRepo.save(adminUser);
        for (AdminUserShopLink ausl : adminUser.getAdminUserShopLinks()) {
            adminUserShopLinkRepo.insert(adminUser.getId(),ausl.getShop_id());
        }
        // Send authentication token in user email
        /*ConfirmationToken confirmationToken = new ConfirmationToken(adminUser);
        confirmationTokenRepo.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(adminUser.getUser_name());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your X-plaza Admin account, please click here: " +"https://xplaza-backend.herokuapp.com/api/adminuser/confirm-account?token="+confirmationToken.getConfirmation_token());
        emailSenderService.sendEmail(mailMessage);*/
    }

    @Transactional
    public void updateAdminUser(AdminUser adminUser) {
        adminUserRepo.update(adminUser.getRole_id(),adminUser.getFull_name(),adminUser.getId());
        adminUserShopLinkRepo.deleteByAdminUserID(adminUser.getId());
        for (AdminUserShopLink ausl : adminUser.getAdminUserShopLinks()) {
            adminUserShopLinkRepo.insert(adminUser.getId(),ausl.getShop_id());
        }
    }

    @Transactional
    public void deleteAdminUser(Long id) {
        //confirmationTokenRepo.deleteByUserID(id);
        adminUserShopLinkRepo.deleteByAdminUserID(id);
        adminUserRepo.deleteById(id);
    }

    public String getAdminUserNameByID(Long id) {
        return adminUserRepo.getName(id);
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

    public void changeAdminUserPassword(String new_password, String salt, String user_name) {
        adminUserRepo.changePassword(new_password,salt,user_name);
    }

    /*public void updateAdminUserConfirmationStatus(Long user_id, Boolean is_confirmed) {
        adminUserRepo.updateConfirmStatus(user_id,is_confirmed);
    }*/
}
