package com.bfsi.agentic.service;

import com.bfsi.agentic.model.SecurityCheckDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Slf4j
@Service
public class SecurityVerificationService {

    public boolean verifyUser(String userId) {
        Scanner scanner = new Scanner(System.in);

        log.debug("Verifying user: " + userId);

        System.out.print("Q1: What was your last transaction amount? ");
        String answer1 = scanner.nextLine();

        System.out.print("Q2: What is your registered device ID? ");
        String answer2 = scanner.nextLine();

        System.out.print("Q3: What city did you last log in from? ");
        String answer3 = scanner.nextLine();

        // Simple mock validation
        if (!"1000".equals(answer1) || !"MI".equalsIgnoreCase(answer2) || !"chennai".equalsIgnoreCase(answer3)) {
            log.debug("Security check failed.");
            return false;
        }
        log.debug("Security check passed.");
        return true;
    }

    public boolean verifySecurityCheck(SecurityCheckDTO securityCheckDTO) {
        if (!"1000".equals(securityCheckDTO.getA1()) || !"MI".equalsIgnoreCase(securityCheckDTO.getA2()) || !"chennai".equalsIgnoreCase(securityCheckDTO.getA3())) {
            log.debug("Security check failed.");
            return false;
        }
        log.debug("Security check passed.");
        return true;
    }
}
