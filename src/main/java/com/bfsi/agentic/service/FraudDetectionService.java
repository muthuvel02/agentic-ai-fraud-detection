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
        String finalOutcome = "APPROVE";

        String amountRisk = reasoningEngineService.evaluateAmountRisk(event);
        System.out.println("Amount Risk: " + amountRisk);

        if ("HIGH".equalsIgnoreCase(amountRisk)) {
            return handleSecurityOrBlock(event, "Amount HIGH");
        } else if ("MEDIUM".equalsIgnoreCase(amountRisk)) {

            String locationRisk = reasoningEngineService.evaluateLocationRisk(event);
            System.out.println("Location Risk: " + locationRisk);

            if ("HIGH".equalsIgnoreCase(locationRisk)) {
                return handleSecurityOrBlock(event, "Location HIGH");
            } else if ("MEDIUM".equalsIgnoreCase(locationRisk)) {

                String deviceRisk = reasoningEngineService.evaluateDeviceRisk(event);
                System.out.println("Device Risk: " + deviceRisk);

                if ("HIGH".equalsIgnoreCase(deviceRisk) || "MEDIUM".equalsIgnoreCase(deviceRisk)) {
                    return handleSecurityOrBlock(event, "Device suspicious");
                }
            }
        }

        saveTransaction(event, "APPROVE");
        return "TRANSACTION SUCCESSFUL";
    }

    private String handleSecurityOrBlock(TransactionEvent event, String reason) {
        boolean passed = securityVerificationService.verifyUser(event.getUserId());

        if (!passed) {
            accountService.freezeAccount(event.getUserId());
            saveTransaction(event, "BLOCKED");

            System.out.println("ALERT: Bank Security Team Notification");
            System.out.println("UserID: " + event.getUserId());
            System.out.println("TransactionID: " + event.getTransactionId());
            System.out.println("Reason: " + reason);
            System.out.println("Action: Account frozen and transaction cancelled.");

            return "TRANSACTION CANCELLED & ACCOUNT FROZEN (" + reason + ") - Bank Security Team Notified";
        } else {
            saveTransaction(event, "APPROVE");
            return "TRANSACTION SUCCESSFUL (Security check passed: " + reason + ")";
        }
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
