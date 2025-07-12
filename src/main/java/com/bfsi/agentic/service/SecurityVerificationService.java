package com.bfsi.agentic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityVerificationService {

    @Autowired
    private EmailService emailService;

    public boolean verifyUser(String userId, String factor, String email, String verificationToken) {
        String fakeLink = "http://localhost:3000/verify?token=" + verificationToken;
        emailService.sendAlertEmail(email, "Verify Transaction", "Demo Link: " + fakeLink);

        return true; // pending until verified
    }

}
