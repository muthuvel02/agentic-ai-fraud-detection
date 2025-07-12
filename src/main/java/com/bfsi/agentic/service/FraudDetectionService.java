package com.bfsi.agentic.service;

import com.bfsi.agentic.model.TransactionRecord;
import com.bfsi.agentic.repository.TransactionRepository;
import com.bfsi.agentic.model.TransactionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FraudDetectionService {

    @Autowired
    private ReasoningEngineService reasoningEngineService;

    @Autowired
    private SecurityVerificationService securityVerificationService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    public String processTransaction(TransactionEvent event) {
        String risk = reasoningEngineService.evaluateRiskWithLLM(event);

        if ("HIGH".equalsIgnoreCase(risk)) {
            return handleSecurityOrBlock(event, "LLM flagged as HIGH");
        }

        saveTransaction(event, "APPROVED");
        return "TRANSACTION SUCCESSFUL";
    }

    private String handleSecurityOrBlock(TransactionEvent event, String reason) {
        // Save transaction with token & question
        TransactionRecord record = saveTransactionAndReturn(event, "PENDING_VERIFICATION");

        securityVerificationService.verifyUser(
                event.getUserId(),
                "DEVICE",
                "demo@example.com",
                record.getVerificationToken()
        );

        return "Transaction is PENDING user approval via verification link.";
    }

    private TransactionRecord saveTransactionAndReturn(TransactionEvent event, String outcome) {
        TransactionRecord record = new TransactionRecord();
        record.setUserId(event.getUserId());
        record.setAmount(event.getAmount());
        record.setLocation(event.getLocation());
        record.setDeviceId(event.getDeviceId());
        record.setRiskOutcome(outcome);
        record.setTimestamp(LocalDateTime.now());

        String token = UUID.randomUUID().toString();
        record.setVerificationToken(token);

        transactionRepository.save(record);
        return record;
    }

    private void saveTransaction(TransactionEvent event, String outcome) {
        TransactionRecord record = new TransactionRecord();
        record.setUserId(event.getUserId());
        record.setAmount(event.getAmount());
        record.setLocation(event.getLocation());
        record.setDeviceId(event.getDeviceId());
        record.setRiskOutcome(outcome);
        record.setTimestamp(LocalDateTime.now());

        transactionRepository.save(record);
    }
}
