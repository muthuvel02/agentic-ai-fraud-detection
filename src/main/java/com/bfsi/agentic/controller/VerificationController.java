package com.bfsi.agentic.controller;

import com.bfsi.agentic.model.TransactionRecord;
import com.bfsi.agentic.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class VerificationController {

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/verify")
    public String verify(@RequestParam String token) {
        // For demo, assume token = transaction ID
        Optional<TransactionRecord> optional = transactionRepository.findById(Long.parseLong(token));

        if (optional.isPresent()) {
            TransactionRecord record = optional.get();
            record.setRiskOutcome("APPROVE");
            transactionRepository.save(record);
            return "Transaction approved! Account restored.";
        } else {
            return "Invalid or expired token.";
        }
    }
}
