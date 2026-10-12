package com.brandshield.service;

import com.brandshield.dto.response.AiExplanationResponse;
import com.brandshield.entity.DetectionEvidence;
import com.brandshield.entity.ThreatDetection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiExplanationOrchestrator {
    private static final Logger log = LoggerFactory.getLogger(AiExplanationOrchestrator.class);

    private final GeminiExplanationService geminiService;
    private final OpenAiExplanationService openAiService;
    private final FallbackExplanationService fallbackService;

    public AiExplanationOrchestrator(
            @Qualifier("geminiExplanationService") GeminiExplanationService geminiService,
            @Qualifier("openAiExplanationService") OpenAiExplanationService openAiService,
            FallbackExplanationService fallbackService) {
        this.geminiService = geminiService;
        this.openAiService = openAiService;
        this.fallbackService = fallbackService;
    }

    public AiExplanationResponse generateExplanation(ThreatDetection detection, List<DetectionEvidence> evidence) {
        // 1. Google Gemini AI (if GEMINI_API_KEY is provided)
        if (geminiService != null && geminiService.isConfigured()) {
            try {
                return geminiService.generateExplanation(detection, evidence);
            } catch (Exception e) {
                log.warn("Gemini AI explanation failed, checking next provider: {}", e.getMessage());
            }
        }

        // 2. OpenAI (if OPENAI_API_KEY is provided)
        if (openAiService != null && openAiService.isConfigured()) {
            try {
                return openAiService.generateExplanation(detection, evidence);
            } catch (Exception e) {
                log.warn("OpenAI explanation failed, falling back to deterministic: {}", e.getMessage());
            }
        }

        // 3. Deterministic Grounded Fallback Engine (no API key needed)
        return fallbackService.generateExplanation(detection, evidence);
    }
}
