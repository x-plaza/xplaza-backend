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
}
