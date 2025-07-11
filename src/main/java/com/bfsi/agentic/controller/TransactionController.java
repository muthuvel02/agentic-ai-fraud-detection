package com.bfsi.agentic.controller;

import com.bfsi.agentic.model.SecurityCheckDTO;
import com.bfsi.agentic.model.TransactionEvent;
import com.bfsi.agentic.model.TransactionEventResponseDTO;
import com.bfsi.agentic.model.TransactionRecord;
import com.bfsi.agentic.repository.TransactionRepository;
import com.bfsi.agentic.service.FraudDetectionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private FraudDetectionService fraudDetectionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping
    public TransactionEventResponseDTO handleTransaction(@RequestBody TransactionEvent event) {
        return fraudDetectionService.processTransaction(event);
    }

    @GetMapping("/api/training-data")
    public List<TransactionRecord> getTrainingData() {
        return transactionRepository.findAll();
    }

    @PostMapping("/initiate-security-check")
    public String initiateSecurityCheck(@RequestHeader("x-auth-token") String token, @Valid @RequestBody SecurityCheckDTO securityCheckDTO) {
        return fraudDetectionService.processSecurityCheck(token, securityCheckDTO);
    }

}
