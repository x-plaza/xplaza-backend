package com.backend.xplaza.service;

import com.backend.xplaza.model.CustomerDetails;
import com.backend.xplaza.repository.CustomerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerUserService {
    @Autowired
    private CustomerUserRepository customerUserRepo;
    @Autowired
    private EmailSenderService emailSenderService;

    public void updateCustomer(CustomerDetails customer) {
        customerUserRepo.save(customer);
    }

    public String getCustomerNameByID(Long id) {
        return customerUserRepo.getUsername(id);
    }

    public void deleteCustomer(Long id) {
        customerUserRepo.deleteById(id);
    }

    public List<CustomerDetails> listCustomers() {
        return customerUserRepo.findAll();
    }

    public CustomerDetails listCustomer(String username) {
        return customerUserRepo.findCustomerByUsername(username);
    }

    public void changeCustomerPassword(String new_password, String salt, String user_name) {
        customerUserRepo.changePassword(new_password,salt,user_name);
    }

    public CustomerDetails getCustomer(Long id) {
        return customerUserRepo.findByCustomerId(id);
    }
}
