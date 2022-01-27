package com.backend.xplaza.service;

import com.backend.xplaza.model.ConfirmationToken;
import com.backend.xplaza.model.PlatformInfo;
import com.backend.xplaza.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepo;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private PlatformInfoService platformInfoService;
    @Autowired
    private Environment env;

    public ConfirmationToken getConfirmationToken(String confirmation_token) {
        return confirmationTokenRepo.findByConfirmationToken(confirmation_token);
    }

    public void sendConfirmationToken(String email) {
        // Send authentication token in the user email
        ConfirmationToken confirmationToken = new ConfirmationToken(email,"Code");
        confirmationTokenRepo.save(confirmationToken);
        // Get platform info
        PlatformInfo platformInfo = platformInfoService.listPlatform();
        // Send email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(env.getProperty("user.mail"));
        mailMessage.setTo(email);
        mailMessage.setSubject("Complete "+ platformInfo.getName() +".com Sign Up!");
        mailMessage.setText("To confirm your "+ platformInfo.getName() +".com Admin account, " +
                "please use the following Code:\n\n" +confirmationToken.getConfirmation_token()+ "\n\n" +
                "With Regards,\n"+ "Team " + platformInfo.getName()
        );
        emailSenderService.sendEmail(mailMessage);
    }

    public void sendOTP(String email) {
        // Send OTP in the user email
        ConfirmationToken confirmationToken = new ConfirmationToken(email,"OTP");
        confirmationTokenRepo.save(confirmationToken);
        // Get platform info
        PlatformInfo platformInfo = platformInfoService.listPlatform();
        // Send email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(env.getProperty("user.mail"));
        mailMessage.setTo(email);
        mailMessage.setSubject("One Time Password!");
        mailMessage.setText("To reset your "+ platformInfo.getName() + ".com Admin account password, " +
                "please use the following OTP:\n\n" + confirmationToken.getConfirmation_token()+ "\n\n" +
                "With Regards,\n"+ "Team " + platformInfo.getName()
        );
        emailSenderService.sendEmail(mailMessage);
    }

    public void sendConfirmationTokenToCustomer(String email) {
        // Send authentication token in the user email
        ConfirmationToken confirmationToken = new ConfirmationToken(email,"Code");
        confirmationTokenRepo.save(confirmationToken);
        // Get platform info
        PlatformInfo platformInfo = platformInfoService.listPlatform();
        // Send email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(env.getProperty("user.mail"));
        mailMessage.setTo(email);
        mailMessage.setSubject("Complete "+ platformInfo.getName() +".com Sign Up!");
        mailMessage.setText("To confirm your email, please use the following Code:\n\n"
                + confirmationToken.getConfirmation_token()+ "\n\n" +
                "With Regards,\n"+ "Team " + platformInfo.getName()
        );
        emailSenderService.sendEmail(mailMessage);
    }

    public void sendOTPToCustomer(String email) {
        // Send OTP in the user email
        ConfirmationToken confirmationToken = new ConfirmationToken(email,"OTP");
        confirmationTokenRepo.save(confirmationToken);
        // Get platform info
        PlatformInfo platformInfo = platformInfoService.listPlatform();
        // Send email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(env.getProperty("user.mail"));
        mailMessage.setTo(email);
        mailMessage.setSubject("One Time Password!");
        mailMessage.setText("To reset your "+ platformInfo.getName() +".com account password, " +
                "please use the following OTP:\n\n"
                + confirmationToken.getConfirmation_token() + "\n\n" +
                "With Regards,\n"+ "Team " + platformInfo.getName()
        );
        emailSenderService.sendEmail(mailMessage);
    }
}
