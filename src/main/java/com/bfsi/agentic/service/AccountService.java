package com.bfsi.agentic.service;

import org.springframework.stereotype.Service;

@Service
public class AccountService {
    public void freezeAccount(String userId) {
        System.out.println("Account for user " + userId + " has been FROZEN due to failed security verification.");
    }
}
