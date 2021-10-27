package com.backend.xplaza.service;

import com.backend.xplaza.model.CustomerDetails;
import com.backend.xplaza.repository.CustomerSignupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class CustomerSignupService {
    @Autowired
    private CustomerSignupRepository customerSignupRepo;
    @Autowired
    private EmailSenderService emailSenderService;

    public void signupCustomer(CustomerDetails customer) {customerSignupRepo.save(customer);}

    public void sendLoginDetails(String email, String temp_password) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("X-Plaza Login Details!");
        mailMessage.setText("Congratulations! Your X-Plaza Customer account has been created successfully.\n\n" +
                "Use the following login details to login to the X-Plaza account.\n\n" +
                "Username : " + email + "\n" + "Password : " + temp_password);
        emailSenderService.sendEmail(mailMessage);
    }

}
