package com.bfsi.agentic.service;

import com.bfsi.agentic.model.TransactionEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReasoningEngineService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RiskResults evaluateTransaction(TransactionEvent event) {
        String amountRisk = getRisk("http://localhost:5000/predict-amount", event, "amount");
        String locationRisk = getRisk("http://localhost:5000/predict-location", event, "location");
        String deviceRisk = getRisk("http://localhost:5000/predict-device", event, "deviceId");

        System.out.printf("Risks -> Amount: %s | Location: %s | Device: %s%n",
                amountRisk, locationRisk, deviceRisk);

        return new RiskResults(amountRisk, locationRisk, deviceRisk);
    }

    private String getRisk(String url, TransactionEvent event, String field) {
        try {
            String requestJson = objectMapper.writeValueAsString(event);
            String response = restTemplate.postForObject(url, event, String.class);
            JsonNode root = objectMapper.readTree(response);
            return root.path("risk").asText().toUpperCase();
        } catch (Exception ex) {
            System.err.println("⚠️ Error calling ML model for " + field + ": " + ex.getMessage());
            return "UNKNOWN";
        }
    }

    public static class RiskResults {
        public String amountRisk;
        public String locationRisk;
        public String deviceRisk;

        public RiskResults(String amountRisk, String locationRisk, String deviceRisk) {
            this.amountRisk = amountRisk;
            this.locationRisk = locationRisk;
            this.deviceRisk = deviceRisk;
        }
    }
}
