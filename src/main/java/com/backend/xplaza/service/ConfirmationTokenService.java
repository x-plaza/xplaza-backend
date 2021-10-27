package com.backend.xplaza.service;

import com.backend.xplaza.model.ConfirmationToken;
import com.backend.xplaza.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepo;
    @Autowired
    private EmailSenderService emailSenderService;

    public ConfirmationToken getConfirmationToken(String confirmation_token) {
        return confirmationTokenRepo.findByConfirmationToken(confirmation_token);
    }

    public void sendConfirmationToken(String email) {
        // Send authentication token in the user email
        ConfirmationToken confirmationToken = new ConfirmationToken(email,"Code");
        confirmationTokenRepo.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Complete Registration!!");
        mailMessage.setText("To confirm your X-plaza Admin account, please use the Code below:\n\n" +confirmationToken.getConfirmation_token());
        emailSenderService.sendEmail(mailMessage);
    }

    public void sendOTP(String email) {
        // Send OTP in the user email
        ConfirmationToken confirmationToken = new ConfirmationToken(email,"OTP");
        confirmationTokenRepo.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("One Time Password!!");
        mailMessage.setText("To reset your X-plaza Admin account password, please use the OTP below:\n\n" +confirmationToken.getConfirmation_token());
        emailSenderService.sendEmail(mailMessage);
    }

    public void sendConfirmationTokenToCustomer(String email) {
        // Send authentication token in the user email
        ConfirmationToken confirmationToken = new ConfirmationToken(email,"Code");
        confirmationTokenRepo.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Complete Registration!!");
        mailMessage.setText("To confirm your email, please use the Code below:\n\n" +confirmationToken.getConfirmation_token());
        emailSenderService.sendEmail(mailMessage);
    }

    public void sendOTPToCustomer(String email) {
        // Send OTP in the user email
        ConfirmationToken confirmationToken = new ConfirmationToken(email,"OTP");
        confirmationTokenRepo.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("One Time Password!!");
        mailMessage.setText("To reset your X-plaza account password, please use the OTP below:\n\n" +confirmationToken.getConfirmation_token());
        emailSenderService.sendEmail(mailMessage);
    }
}
