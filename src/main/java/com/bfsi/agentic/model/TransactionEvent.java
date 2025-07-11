package com.bfsi.agentic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionEvent {
    private String transactionId;
    private String userId;
    private Double amount;
    private String location;
    private String deviceId;
    private String ip;
    private String outcome;
}
