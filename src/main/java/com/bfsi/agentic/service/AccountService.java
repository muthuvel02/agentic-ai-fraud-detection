package com.bfsi.agentic.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountService {
    public void freezeAccount(String userId) {
        log.warn("Account for user - {} has been FROZEN due to failed security verification", userId);
    }
}
