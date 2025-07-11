package com.bfsi.agentic.model;

public class TransactionEvent {
    private String userId;
    private double amount;
    private String location;
    private String deviceId;
    private String transactionId;

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    @Override
    public String toString() {
        return String.format("UserId: %s, Amount: %.2f, Location: %s, DeviceId: %s, TransactionId: %s",
                userId, amount, location, deviceId, transactionId);
    }
}
