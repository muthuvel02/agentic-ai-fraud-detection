package com.bfsi.agentic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SecurityVerificationService {

    @Value("${verify.txn.url.prefix}")
    private String verifyTxnUrlPrefix;

    @Autowired
    private EmailService emailService;

    public String verifyUser(String userId, String factor, String email, Long transactionId) {
        String verifyLink = String.format(verifyTxnUrlPrefix, transactionId);
        emailService.sendAlertEmail(email, "Verify Transaction", "Demo Link: " + verifyLink);

        return verifyLink; // pending until verified
    }

}
