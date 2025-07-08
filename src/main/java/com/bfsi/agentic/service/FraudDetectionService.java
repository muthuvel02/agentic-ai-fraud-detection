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
    private EmailService emailService;

    @Autowired
    private TransactionRepository transactionRepository;

    public String processTransaction(TransactionEvent event) {
        ReasoningEngineService.RiskResults risks = reasoningEngineService.evaluateTransaction(event);

        boolean amountSuspicious = "HIGH".equals(risks.amountRisk) || "MEDIUM".equals(risks.amountRisk);
        boolean locationSuspicious = "HIGH".equals(risks.locationRisk) || "MEDIUM".equals(risks.locationRisk);
        boolean deviceSuspicious = "HIGH".equals(risks.deviceRisk) || "MEDIUM".equals(risks.deviceRisk);

        boolean anyHighRisk = "HIGH".equals(risks.amountRisk) || "HIGH".equals(risks.locationRisk) || "HIGH".equals(risks.deviceRisk);

        String finalOutcome;

        // Always ask security questions if any HIGH or all suspicious
        if (anyHighRisk || (amountSuspicious && locationSuspicious && deviceSuspicious)) {
            boolean passed = securityVerificationService.verifyUser(event.getUserId());
            if (!passed) {
                accountService.freezeAccount(event.getUserId());
                finalOutcome = "BLOCKED";
                saveTransaction(event, finalOutcome);
                return "TRANSACTION CANCELLED & ACCOUNT FROZEN";
            } else {
                finalOutcome = "APPROVE";
                saveTransaction(event, finalOutcome);
                return "TRANSACTION SUCCESSFUL (Security check passed)";
            }
        }

        // Else approve
        finalOutcome = "APPROVE";
        saveTransaction(event, finalOutcome);
        return "TRANSACTION SUCCESSFUL";
    }

    private void saveTransaction(TransactionEvent event, String riskOutcome) {
        TransactionRecord record = new TransactionRecord();
        record.setUserId(event.getUserId());
        record.setAmount(event.getAmount());
        record.setLocation(event.getLocation());
        record.setDeviceId(event.getDeviceId());
        record.setRiskOutcome(riskOutcome);
        record.setTimestamp(LocalDateTime.now());
        transactionRepository.save(record);

        System.out.println("Stored transaction with outcome: " + riskOutcome);
    }
}
