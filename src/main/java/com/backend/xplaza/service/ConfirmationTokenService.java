package com.backend.xplaza.service;

import com.backend.xplaza.model.ConfirmationToken;
import com.backend.xplaza.repository.ConfirmationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepo;

    public ConfirmationToken getConfirmationToken(String confirmation_token) {
        return confirmationTokenRepo.findByConfirmationToken(confirmation_token);
    }
}
