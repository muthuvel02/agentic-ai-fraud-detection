package com.bfsi.agentic.service;

import com.bfsi.agentic.model.TransactionEvent;
import com.bfsi.agentic.model.TransactionRecord;
import com.bfsi.agentic.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class ReasoningEngineService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("classpath:prompts/fraud_prompt.txt")
    private Resource fraudPromptTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public String evaluateRiskWithLLM(TransactionEvent event) {
        List<TransactionRecord> history = transactionRepository
                .findTop5ByUserIdOrderByTimestampDesc(event.getUserId());

        StringBuilder historyText = new StringBuilder();
        for (TransactionRecord rec : history) {
            historyText.append(String.format(
                    "Amount: %.2f, Location: %s, Device: %s, Time: %s, Outcome: %s\n",
                    rec.getAmount(), rec.getLocation(), rec.getDeviceId(),
                    rec.getTimestamp(), rec.getRiskOutcome()
            ));
        }

        try {
            String basePrompt = StreamUtils.copyToString(fraudPromptTemplate.getInputStream(), StandardCharsets.UTF_8);

            String prompt = basePrompt
                    .replace("{HISTORY}", historyText.toString())
                    .replace("{AMOUNT}", String.valueOf(event.getAmount()))
                    .replace("{LOCATION}", event.getLocation())
                    .replace("{DEVICE}", event.getDeviceId());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(groqApiKey);

            Map<String, Object> request = Map.of(
                    "model", "llama3-70b-8192",
                    "messages", List.of(
                            Map.of("role", "system", "content", "You are a fraud detection expert."),
                            Map.of("role", "user", "content", prompt)
                    )
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.groq.com/openai/v1/chat/completions",
                    entity,
                    String.class
            );

            String answer = response.getBody();
            System.out.println("Groq LLM Answer: " + answer);

            if (answer != null && answer.toUpperCase().contains("HIGH")) return "HIGH";
            if (answer != null && answer.toUpperCase().contains("MEDIUM")) return "MEDIUM";
            return "LOW";

        } catch (Exception e) {
            System.out.println(" LLM call failed, fallback to LOW risk");
            return "LOW";
        }
    }
}
