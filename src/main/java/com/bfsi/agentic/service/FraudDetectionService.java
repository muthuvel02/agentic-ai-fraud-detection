package com.bfsi.agentic.service;

import com.bfsi.agentic.model.SecurityCheckDTO;
import com.bfsi.agentic.model.TransactionEvent;
import com.bfsi.agentic.model.TransactionEventResponseDTO;
import com.bfsi.agentic.model.TransactionRecord;
import com.bfsi.agentic.repository.TransactionRepository;
import com.bfsi.agentic.util.JWTUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

import static com.bfsi.agentic.enums.RiskEnum.HIGH;
import static com.bfsi.agentic.enums.RiskEnum.LOW;

@Slf4j
@Service
public class FraudDetectionService {

    @Autowired
    private ReasoningEngineService reasoningEngineService;

    @Autowired
    private SecurityVerificationService securityVerificationService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionEventResponseDTO processTransaction(TransactionEvent event) {
        String finalOutcome = "APPROVE";

        String amountRisk = "HIGH";//reasoningEngineService.evaluateAmountRisk(event);
        log.debug("Amount Risk: " + amountRisk);

        if (HIGH.name().equalsIgnoreCase(amountRisk)) {
            return handleSecurityOrBlock(event, "Amount HIGH");
        } else if (LOW.name().equalsIgnoreCase(amountRisk)) {

            String locationRisk = "HIGH";//reasoningEngineService.evaluateLocationRisk(event);
            log.debug("Location Risk: " + locationRisk);

            if (HIGH.name().equalsIgnoreCase(locationRisk)) {
                return handleSecurityOrBlock(event, "Location HIGH");
            } else if (LOW.name().equalsIgnoreCase(locationRisk)) {

                String deviceRisk = "HIGH";//reasoningEngineService.evaluateDeviceRisk(event);
                log.debug("Device Risk: " + deviceRisk);

                if (HIGH.name().equalsIgnoreCase(deviceRisk) || LOW.name().equalsIgnoreCase(deviceRisk)) {
                    return handleSecurityOrBlock(event, "Device suspicious");
                }
            }
        }

        saveTransaction(event, "APPROVE");
        return TransactionEventResponseDTO.builder()
                .risk("TRANSACTION SUCCESSFUL")
                .build();
    }

    private TransactionEventResponseDTO handleSecurityOrBlock(TransactionEvent event, String reason) {
        boolean passed = securityVerificationService.verifyUser(event.getUserId());
        String token;

        if (!passed) {
            accountService.freezeAccount(event.getUserId());
            saveTransaction(event, "BLOCKED");

            log.debug("ALERT: Bank Security Team Notification");
            log.debug("UserID: " + event.getUserId());
            log.debug("TransactionID: " + event.getTransactionId());
            log.debug("Reason: " + reason);
            log.debug("Action: Account frozen and transaction cancelled.");

            token = generateToken(event, reason);
            return TransactionEventResponseDTO.builder()
                    .risk("TRANSACTION CANCELLED & ACCOUNT FROZEN (" + reason + ") - Bank Security Team Notified")
                    .token(token)
                    .build();
        } else {
            saveTransaction(event, "APPROVE");
            token = generateToken(event, reason);
            return TransactionEventResponseDTO.builder()
                    .risk("TRANSACTION SUCCESSFUL (Security check passed: " + reason + ")")
                    .token(token)
                    .build();
        }
    }

    private String generateToken(TransactionEvent event, String reason) {

        Map<String, String> claims = Map.of(
                "transactionID", event.getTransactionId(),
                "userId", event.getUserId(),
                "amount", event.getAmount().toString(),
                "location", event.getLocation(),
                "deviceId", event.getDeviceId(),
                "ip", event.getIp(),
                "reason", reason,
                "flag", "HIGH"
        );
        return jwtUtil.generateToken(claims);
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

    public String processSecurityCheck(String token, SecurityCheckDTO securityCheckDTO) {
        boolean securityCheckFlag = securityVerificationService.verifySecurityCheck(securityCheckDTO);
        TransactionEvent event = new TransactionEvent();
        String reason = parseToken(token, event);
        if (!securityCheckFlag) {
            accountService.freezeAccount(event.getUserId());
            saveTransaction(event, "BLOCKED");

            log.debug("ALERT: Bank Security Team Notification");
            log.debug("UserID: " + event.getUserId());
            log.debug("TransactionID: " + event.getTransactionId());
            log.debug("Reason: " + reason);
            log.debug("Action: Account frozen and transaction cancelled.");

            return "TRANSACTION CANCELLED & ACCOUNT FROZEN (" + reason + ") - Bank Security Team Notified";
        } else {
            saveTransaction(event, "APPROVE");
            return "TRANSACTION SUCCESSFUL (Security check passed: " + reason + ")";
        }
    }

    private String parseToken(String token, TransactionEvent event) {
        Claims claims = jwtUtil.getClaims(token);
        if (claims != null) {
            event.setTransactionId(claims.get("transactionID", String.class));
            event.setUserId(claims.get("userId", String.class));
            event.setAmount(Double.parseDouble(claims.get("amount", String.class)));
            event.setLocation(claims.get("location", String.class));
            event.setDeviceId(claims.get("deviceId", String.class));
            event.setIp(claims.get("ip", String.class));
            return claims.get("reason", String.class);
        }
        return null;
    }
}
