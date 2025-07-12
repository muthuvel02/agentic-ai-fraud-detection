package com.bfsi.agentic.controller;

import com.bfsi.agentic.model.TransactionRecord;
import com.bfsi.agentic.repository.TransactionRepository;
import com.bfsi.agentic.DTO.VerificationRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class VerificationController {

    @Autowired
    private TransactionRepository repository;

    @GetMapping("/verify")
    public ResponseEntity<?> getTransaction(@RequestParam String token) {

        Optional<TransactionRecord> txnOpt = repository.findByVerificationToken(token);

        if (txnOpt.isEmpty()) {
           log.warn("Not found in DB!");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(txnOpt.get());
    }

    @PostMapping("/verify/confirm")
    public ResponseEntity<String> confirmTransaction(@RequestBody VerificationRequest request) {
        if (request.getToken() == null || request.getToken().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Missing verification token.");
        }

        Optional<TransactionRecord> txnOpt = repository.findByVerificationToken(request.getToken());

        if (txnOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Invalid or expired verification link.");
        }

        TransactionRecord txn = txnOpt.get();

        if ("APPROVE".equalsIgnoreCase(request.getAction())) {
            txn.setRiskOutcome("APPROVED");
            repository.save(txn);
            return ResponseEntity.ok("Transaction approved.");
        } else if ("DECLINE".equalsIgnoreCase(request.getAction())) {
            txn.setRiskOutcome("DECLINED");
            repository.save(txn);
            return ResponseEntity.ok("Transaction declined.");
        }

        return ResponseEntity.badRequest().body("Invalid action. Must be APPROVE or DECLINE.");
    }

}
