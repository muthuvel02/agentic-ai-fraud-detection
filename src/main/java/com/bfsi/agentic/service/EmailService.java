package com.bfsi.agentic.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendAlertEmail(String toEmail, String subject, String body) {
        System.out.println("[MOCK EMAIL] To: " + toEmail);
        System.out.println("Subject: " + subject);
        System.out.println("Body:\n" + body);
    }
}
