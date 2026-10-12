package com.brandshield.service;

import com.brandshield.dto.response.AiExplanationResponse;
import com.brandshield.entity.DetectionEvidence;
import com.brandshield.entity.ThreatDetection;
import com.brandshield.exception.AiServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("openAiExplanationService")
public class OpenAiExplanationService implements AiExplanationService {

    private static final Logger log = LoggerFactory.getLogger(OpenAiExplanationService.class);

    @Value("${app.ai.openai.api-key:}")
    private String apiKey;

    @Value("${app.ai.openai.model:gpt-4o-mini}")
    private String model;

    @Value("${app.ai.openai.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public OpenAiExplanationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }

    @Override
    public AiExplanationResponse generateExplanation(ThreatDetection detection, List<DetectionEvidence> evidence) {
        if (!isConfigured()) {
            throw new AiServiceException("OpenAI API key not configured");
        }

        try {
            String prompt = buildPrompt(detection, evidence);
            String response = callOpenAi(prompt);
            return parseResponse(response);
        } catch (AiServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("OpenAI API call failed: {}", e.getMessage(), e);
            throw new AiServiceException("Failed to generate AI explanation: " + e.getMessage(), e);
        }
    }

    private String buildPrompt(ThreatDetection detection, List<DetectionEvidence> evidence) {
        StringBuilder sb = new StringBuilder();
        sb.append("You are a cybersecurity analyst assistant. Analyze the detection evidence and return JSON.\n\n");
        sb.append("DETECTION CONTEXT:\n");
        sb.append("- Brand: ").append(detection.getBrand().getName()).append("\n");
        sb.append("- Candidate Type: ").append(detection.getCandidateType()).append("\n");
        sb.append("- Risk Score: ").append(detection.getRiskScore()).append(" (").append(detection.getRiskLevel()).append(")\n");
        sb.append("\nEVIDENCE:\n");
        for (DetectionEvidence e : evidence) {
            sb.append("- [").append(e.getSignalCategory()).append("] ").append(e.getSignalName())
              .append(": ").append(e.getEvidenceExplanation()).append(" (Score: ").append(e.getScoreContribution()).append(")\n");
        }
        sb.append("\nReturn JSON only with keys: summary (string), observedFacts (string[]), reasonsForConcern (string[]), uncertainties (string[]), recommendedNextSteps (string[])");
        return sb.toString();
    }

    private String callOpenAi(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = Map.of(
            "model", model,
            "messages", List.of(
                Map.of("role", "system", "content", "You are a cybersecurity expert. Reply only with valid JSON."),
                Map.of("role", "user", "content", prompt)
            ),
            "temperature", 0.2
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/chat/completions", HttpMethod.POST, entity, String.class);
        if (response.getBody() == null) throw new AiServiceException("Empty response from AI");
        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();
        } catch (Exception e) {
            throw new AiServiceException("Error parsing AI response: " + e.getMessage(), e);
        }
    }

    private AiExplanationResponse parseResponse(String raw) {
        try {
            String clean = raw.trim();
            if (clean.startsWith("```")) {
                clean = clean.replaceFirst("```json?\\s*", "").replaceFirst("```\\s*$", "");
            }
            JsonNode n = objectMapper.readTree(clean);
            return AiExplanationResponse.builder()
                .summary(n.path("summary").asText("Threat assessment completed."))
                .observedFacts(toList(n.path("observedFacts")))
                .reasonsForConcern(toList(n.path("reasonsForConcern")))
                .uncertainties(toList(n.path("uncertainties")))
                .recommendedNextSteps(toList(n.path("recommendedNextSteps")))
                .generatedAt(LocalDateTime.now())
                .modelUsed(model)
                .fallback(false)
                .build();
        } catch (Exception e) {
            return AiExplanationResponse.builder()
                .summary(raw)
                .observedFacts(List.of())
                .reasonsForConcern(List.of())
                .uncertainties(List.of())
                .recommendedNextSteps(List.of())
                .generatedAt(LocalDateTime.now())
                .modelUsed(model)
                .fallback(false)
                .build();
        }
    }

    private List<String> toList(JsonNode arr) {
        List<String> list = new ArrayList<>();
        if (arr.isArray()) {
            for (JsonNode item : arr) list.add(item.asText());
        }
        return list;
    }
}
