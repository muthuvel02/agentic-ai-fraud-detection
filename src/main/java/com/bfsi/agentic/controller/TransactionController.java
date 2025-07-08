package com.bfsi.agentic.controller;

import com.bfsi.agentic.model.TransactionEvent;
import com.bfsi.agentic.model.TransactionRecord;
import com.bfsi.agentic.repository.TransactionRepository;
import com.bfsi.agentic.service.FraudDetectionService;
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
    public String handleTransaction(@RequestBody TransactionEvent event) {
        return fraudDetectionService.processTransaction(event);
    }

    @GetMapping("/api/training-data")
    public List<TransactionRecord> getTrainingData() {
        return transactionRepository.findAll();
    }

}
