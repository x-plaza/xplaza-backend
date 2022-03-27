package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.model.ConfirmationToken;
import com.backend.xplaza.model.CustomerDetails;
import com.backend.xplaza.model.CustomerLogin;
import com.backend.xplaza.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.telesign.MessagingClient;
import com.telesign.RestClient;
import com.telesign.Util;

@RestController
@RequestMapping("/api/customer-signup")
public class CustomerSignupController {
    @Autowired
    private CustomerSignupService customerSignupService;
    @Autowired
    private CustomerUserService customerUserService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CustomerLoginService customerLoginService;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    private Date start, end;
    private Long responseTime;

    @ModelAttribute
    public void setResponseHeader(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setHeader("Expires", "0"); // Proxies.
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Set-Cookie", "type=ninja");
    }

    @PostMapping(value= "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> signupCustomer (@RequestBody @Valid CustomerDetails customerDetails) {
        start = new Date();
        ConfirmationToken token = confirmationTokenService.getConfirmationToken(customerDetails.getOtp());
        if(token == null) {
            end = new Date();
            responseTime = end.getTime() - start.getTime();
            return new ResponseEntity<>(new ApiResponse(responseTime, "Signup Customer", HttpStatus.FORBIDDEN.value(),
                    "Failed", "Confirmation code does not match!", null), HttpStatus.FORBIDDEN);
        }
        if(!token.getEmail().equals(customerDetails.getEmail().toLowerCase())) {
            end = new Date();
            responseTime = end.getTime() - start.getTime();
            return new ResponseEntity<>(new ApiResponse(responseTime, "Signup Customer", HttpStatus.FORBIDDEN.value(),
                    "Failed", "Confirmation code does not match!", null), HttpStatus.FORBIDDEN);
        }
        Date today = new Date();
        if(today.after(token.getValid_till())) {
            end = new Date();
            responseTime = end.getTime() - start.getTime();
            return new ResponseEntity<>(new ApiResponse(responseTime, "Signup Customer", HttpStatus.FORBIDDEN.value(),
                    "Failed", "Confirmation code expired! Please get a new code!", null), HttpStatus.FORBIDDEN);
        }
        CustomerLogin customerLogin = customerLoginService.getCustomerLoginDetails(customerDetails.getEmail().toLowerCase());
        if(customerLogin != null) {
            end = new Date();
            responseTime = end.getTime() - start.getTime();
            return new ResponseEntity<>(new ApiResponse(responseTime, "Signup Customer", HttpStatus.FORBIDDEN.value(),
                    "Failed", "User Already Exist!", null), HttpStatus.FORBIDDEN);
        }
        // Encrypt Password with Salt
        String temp_password = customerDetails.getPassword();
        byte[] byteSalt = null;
        try {
            byteSalt = securityService.getSalt();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger("Salt error").log(Level.SEVERE, null, ex);
        }
        byte[] biteDigestPsw = securityService.getSaltedHashSHA512(customerDetails.getPassword(), byteSalt);
        String strDigestPsw = securityService.toHex(biteDigestPsw);
        String strSalt = securityService.toHex(byteSalt);
        customerDetails.setPassword(strDigestPsw);
        customerDetails.setSalt(strSalt);
        customerDetails.setEmail(customerDetails.getEmail().toLowerCase());
        customerSignupService.signupCustomer(customerDetails);
        customerSignupService.sendLoginDetails(customerDetails.getEmail(),temp_password);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Signup Customer", HttpStatus.CREATED.value(),"Success",
                "Customer account has been created successfully.",null), HttpStatus.CREATED);
    }


    @PostMapping(value= "/send-otp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> sendOTP (@RequestParam("cell_no") @Valid String cell_no) {
        start = new Date();
        String customerId = "D22F37FA-1914-48E3-B018-ACFF0E611C3C";
        String apiKey = "1UKbxwwvd0J7q5ne4vtTAlnE4DVqoVqQ+yvKKyHEISDXUu35Jzv7UIeXTlFcR4IYlIp/+uj2EpvXLbksmu/xkA==";
        String phoneNumber = cell_no;
        String verifyCode = Util.randomWithNDigits(5);
        String message = String.format("Your OTP is %s", verifyCode) + "\n\nTeam Xwinkel";
        String messageType = "OTP";
        try {
            MessagingClient messagingClient = new MessagingClient(customerId, apiKey);
            RestClient.TelesignResponse telesignResponse = messagingClient.message(phoneNumber, message, messageType, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Send OTP", HttpStatus.CREATED.value(),"Success",
                "An OTP has been sent to your phone.",null), HttpStatus.CREATED);
    }
}
