package com.bfsi.agentic.model;

public class TransactionEvent {
    private String transactionId;
    private String userId;
    private Double amount;
    private String location;
    private String deviceId;
    private String ip;

    private String outcome;

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
}
