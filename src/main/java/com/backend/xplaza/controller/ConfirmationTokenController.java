package com.backend.xplaza.controller;

import com.backend.xplaza.common.ApiResponse;
import com.backend.xplaza.service.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/api/confirmation-token")
public class ConfirmationTokenController {
    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    private Date start, end;
    private Long responseTime;

    @PostMapping(value= "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> sendConfirmationToken (@RequestParam("username") @Valid String username) {
        start = new Date();
        confirmationTokenService.sendConfirmationToken(username);
        end = new Date();
        responseTime = end.getTime() - start.getTime();
        return new ResponseEntity<>(new ApiResponse(responseTime, "Send Confirmation OTP", HttpStatus.CREATED.value(),"Success", "A confirmation token has been sent to the email.",null), HttpStatus.CREATED);
    }
}
