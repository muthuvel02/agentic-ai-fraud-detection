package com.bfsi.agentic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityVerificationService {

    @Autowired
    private EmailService emailService;

    public boolean verifyUser(String userId, String factor, String email, Long transactionId) {
        String fakeLink = "http://localhost:8081/verify?token=" + transactionId;
        emailService.sendAlertEmail(email, "Verify Transaction", "Demo Link: " + fakeLink);

        return false; // pending until verified
    }

}
