package com.bfsi.agentic.service;

import com.bfsi.agentic.model.TransactionEvent;
import com.bfsi.agentic.model.TransactionRecord;
import com.bfsi.agentic.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

        saveTransaction(event, "APPROVE");
        return "✅ TRANSACTION SUCCESSFUL";
    }

    private String handleSecurityOrBlock(TransactionEvent event, String reason) {
        // Save and get the real transaction ID
        TransactionRecord record = saveTransactionAndReturn(event, "PENDING_VERIFICATION");

        // Pass the real ID to generate unique verification link
        securityVerificationService.verifyUser(
                event.getUserId(),
                "DEVICE",  // Example factor
                "demo@example.com",  // Mock email
                record.getId()  // Unique ID for this transaction
        );

        return "⏳ Transaction is PENDING user approval via verification link.";
    }

    private TransactionRecord saveTransactionAndReturn(TransactionEvent event, String outcome) {
        TransactionRecord record = new TransactionRecord();
        record.setUserId(event.getUserId());
        record.setAmount(event.getAmount());
        record.setLocation(event.getLocation());
        record.setDeviceId(event.getDeviceId());
        record.setRiskOutcome(outcome);
        record.setTimestamp(LocalDateTime.now());
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
