package com.backend.xplaza.service;

import com.backend.xplaza.model.Brand;
import com.backend.xplaza.model.CustomerDetails;
import com.backend.xplaza.repository.CustomerSignupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerSignupService {
    @Autowired
    private CustomerSignupRepository customerSignupRepo;
    @Autowired
    private EmailSenderService emailSenderService;

    public void addCustomer(CustomerDetails customer) {customerSignupRepo.save(customer);}
    public void updateCustomer(CustomerDetails customer) {
        customerSignupRepo.save(customer);
    }
    public String getCustomerNameByID(Long id) {
        return customerSignupRepo.getUsername(id);
    }
    public void deleteCustomer(Long id) {
        customerSignupRepo.deleteById(id);
    }
    public List<CustomerDetails> listCustomers() {
        return customerSignupRepo.findAll();
    }
    public CustomerDetails listCustomer(String email) {
        return customerSignupRepo.findUserByUsername(email);
    }

    public void changeCustomerPassword(String new_password, String salt, String user_name) {
        customerSignupRepo.changePassword(new_password,salt,user_name);
    }

}
