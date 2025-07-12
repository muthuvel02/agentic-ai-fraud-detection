package com.bfsi.agentic.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {
    public void sendAlertEmail(String toEmail, String subject, String body) {
        log.debug("[MOCK EMAIL] To: " + toEmail);
        log.debug("Subject: " + subject);
        log.debug("Body:\n" + body);
    }
}
