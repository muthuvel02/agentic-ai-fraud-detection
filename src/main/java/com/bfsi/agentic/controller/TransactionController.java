package com.bfsi.agentic.controller;

import com.bfsi.agentic.model.TransactionEvent;
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
    public ResponseEntity<String> processTransaction(@RequestBody TransactionEvent event) {
        String result = fraudDetectionService.processTransaction(event);
        return ResponseEntity.ok(result);
    }
}
