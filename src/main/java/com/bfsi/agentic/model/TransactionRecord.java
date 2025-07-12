package com.bfsi.agentic.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transaction_record")
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private double amount;
    private String location;
    private String deviceId;
    private String riskOutcome;
    private LocalDateTime timestamp;
    private String verificationToken;


}
