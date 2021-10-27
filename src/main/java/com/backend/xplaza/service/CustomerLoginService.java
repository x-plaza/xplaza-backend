package com.backend.xplaza.service;

import com.backend.xplaza.model.CustomerDetails;
import com.backend.xplaza.model.CustomerLogin;
import com.backend.xplaza.repository.CustomerLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class CustomerLoginService {
    @Autowired
    private CustomerUserService customerUserService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private CustomerLoginRepository customerLoginRepo;

    public Boolean isVaidUser (String username, String password) throws IOException {
        CustomerDetails customer = customerUserService.listCustomer(username);
        String strOrgSalt = customer.getSalt();
        byte[] byteSalt = securityService.fromHex(strOrgSalt);
        byte[] loginPassword = securityService.getSaltedHashSHA512(password,byteSalt);
        byte[] storedPassword = securityService.fromHex(customer.getPassword());
        boolean result = Arrays.equals(loginPassword, storedPassword);
        return result;
    }

    public CustomerLogin getCustomerLoginDetails(String username) {
        return customerLoginRepo.findCustomerByUsername(username);
    }
}
