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
        ConfirmationToken confirmationToken = new ConfirmationToken(email);
        confirmationTokenRepo.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your X-plaza Admin account, use the code below:\n" +confirmationToken.getConfirmation_token());
        emailSenderService.sendEmail(mailMessage);
    }
}
