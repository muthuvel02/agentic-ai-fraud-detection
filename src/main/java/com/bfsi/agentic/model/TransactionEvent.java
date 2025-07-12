package com.bfsi.agentic.model;

import lombok.Data;

@Data
public class TransactionEvent {
    private String userId;
    private double amount;
    private String location;
    private String deviceId;
    private String transactionId;
}
