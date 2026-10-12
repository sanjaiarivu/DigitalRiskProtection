package com.brandshield.service;

import com.brandshield.dto.response.AiExplanationResponse;
import com.brandshield.entity.DetectionEvidence;
import com.brandshield.entity.ThreatDetection;

import java.util.List;

/**
 * Interface for AI-powered threat explanation generation.
 * Implementations may use external AI APIs or deterministic algorithms.
 */
public interface AiExplanationService {
    AiExplanationResponse generateExplanation(ThreatDetection detection, List<DetectionEvidence> evidence);
}
