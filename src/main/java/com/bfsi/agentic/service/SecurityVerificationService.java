package com.bfsi.agentic.service;

import org.springframework.stereotype.Service;
import java.util.Scanner;

@Service
public class SecurityVerificationService {

    public boolean verifyUser(String userId) {
        // Simulate asking 3 security questions (real use: OTP, app push, secure web page)
        Scanner scanner = new Scanner(System.in);

        System.out.println("Verifying user: " + userId);

        System.out.print("Q1: What was your last transaction amount? ");
        String answer1 = scanner.nextLine();

        System.out.print("Q2: What is your registered device ID? ");
        String answer2 = scanner.nextLine();

        System.out.print("Q3: What city did you last log in from? ");
        String answer3 = scanner.nextLine();

        // Simple mock validation
        if ("1000".equals(answer1) && "MI".equalsIgnoreCase(answer2) && "chennai".equalsIgnoreCase(answer3)) {
            System.out.println("Security check passed.");
            return true;
        } else {
            System.out.println("Security check failed.");
            return false;
        }
    }
}
