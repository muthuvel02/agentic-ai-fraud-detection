package com.bfsi.agentic.controller;

import com.bfsi.agentic.model.TransactionEvent;
import com.bfsi.agentic.model.ValidateTransactionResponseDTO;
import com.bfsi.agentic.service.FraudDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private FraudDetectionService fraudDetectionService;

    @PostMapping
    public ResponseEntity<ValidateTransactionResponseDTO> processTransaction(@RequestBody TransactionEvent event) {
        ValidateTransactionResponseDTO result = fraudDetectionService.processTransaction(event);
        return ResponseEntity.ok(result);
    }
}
