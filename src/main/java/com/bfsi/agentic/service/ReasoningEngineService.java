package com.bfsi.agentic.service;

import com.bfsi.agentic.model.TransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ReasoningEngineService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String evaluateAmountRisk(TransactionEvent event) {
        String response = restTemplate.postForObject("http://localhost:5000/predict-amount", event, String.class);
        log.debug("Amount ML Risk: " + response);
        return parseRisk(response);
    }

    public String evaluateLocationRisk(TransactionEvent event) {
        String response = restTemplate.postForObject("http://localhost:5000/predict-location", event, String.class);
        log.debug("Location ML Risk: " + response);
        return parseRisk(response);
    }

    public String evaluateDeviceRisk(TransactionEvent event) {
        String response = restTemplate.postForObject("http://localhost:5000/predict-device", event, String.class);
        log.debug("Device ML Risk: " + response);
        return parseRisk(response);
    }

    private String parseRisk(String response) {
        if (response != null && response.contains("HIGH")) return "HIGH";
        if (response != null && response.contains("MEDIUM")) return "MEDIUM";
        return "LOW";
    }
}
