package com.brandshield.service;

import com.brandshield.dto.response.AiExplanationResponse;
import com.brandshield.entity.DetectionEvidence;
import com.brandshield.entity.ThreatDetection;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Deterministic fallback explanation service.
 * Generates structured threat explanations from detection evidence
 * without relying on an external AI API.
 *
 * Used as a fallback when the AI API is unavailable or not configured.
 */
@Service
public class FallbackExplanationService implements AiExplanationService {

    @Override
    public AiExplanationResponse generateExplanation(ThreatDetection detection, List<DetectionEvidence> evidence) {
        String summary = buildSummary(detection, evidence);
        List<String> facts = buildObservedFacts(evidence);
        List<String> concerns = buildConcerns(detection, evidence);
        List<String> uncertainties = buildUncertainties(detection, evidence);
        List<String> nextSteps = buildNextSteps(detection);

        return AiExplanationResponse.builder()
                .summary(summary)
                .observedFacts(facts)
                .reasonsForConcern(concerns)
                .uncertainties(uncertainties)
                .recommendedNextSteps(nextSteps)
                .generatedAt(LocalDateTime.now())
                .modelUsed("deterministic-fallback")
                .fallback(true)
                .build();
    }

    private String buildSummary(ThreatDetection detection, List<DetectionEvidence> evidence) {
        if (detection.getIsOfficialAsset() != null && detection.getIsOfficialAsset()) {
            return String.format("This %s candidate has been verified as an official asset of %s. No action required.",
                detection.getCandidateType().name().toLowerCase(), detection.getBrand().getName());
        }

        return String.format(
            "Analysis identified %d signal(s) indicating this %s candidate may be impersonating the brand '%s'. " +
            "The overall risk score is %d (%s). This is an automatically flagged candidate requiring human review.",
            evidence.size(),
            detection.getCandidateType().name().toLowerCase(),
            detection.getBrand().getName(),
            detection.getRiskScore(),
            detection.getRiskLevel().name()
        );
    }

    private List<String> buildObservedFacts(List<DetectionEvidence> evidence) {
        return evidence.stream()
                .map(e -> e.getEvidenceExplanation())
                .collect(Collectors.toList());
    }

    private List<String> buildConcerns(ThreatDetection detection, List<DetectionEvidence> evidence) {
        List<String> concerns = new ArrayList<>();

        boolean hasCharSub = evidence.stream()
            .anyMatch(e -> "CHARACTER_SUBSTITUTION".equals(e.getSignalCategory()));
        boolean hasSuspiciousLink = evidence.stream()
            .anyMatch(e -> "SUSPICIOUS_LINK".equals(e.getSignalCategory()));
        boolean hasPublisherMismatch = evidence.stream()
            .anyMatch(e -> "PUBLISHER_MISMATCH".equals(e.getSignalCategory()));
        boolean hasHighNameSim = evidence.stream()
            .anyMatch(e -> "NAME_SIMILARITY".equals(e.getSignalCategory()) && e.getScoreContribution() >= 20);

        if (hasCharSub) {
            concerns.add("Uses character substitutions (look-alike characters) in the name, a common impersonation technique.");
        }
        if (hasSuspiciousLink) {
            concerns.add("Contains links associated with URL shorteners or suspicious domains, which may indicate phishing.");
        }
        if (hasPublisherMismatch) {
            concerns.add("The publisher/developer does not match any registered official publisher for this brand.");
        }
        if (hasHighNameSim) {
            concerns.add("The name has high similarity to the monitored brand, which could mislead users.");
        }
        if (detection.getRiskScore() >= 76) {
            concerns.add("The combined risk signals result in a CRITICAL risk score, suggesting immediate investigation.");
        }

        if (concerns.isEmpty()) {
            concerns.add("Minor similarity signals detected. May be coincidental.");
        }

        return concerns;
    }

    private List<String> buildUncertainties(ThreatDetection detection, List<DetectionEvidence> evidence) {
        List<String> uncertainties = new ArrayList<>();
        uncertainties.add("Similarity alone does not prove malicious intent. A human analyst must review the evidence.");
        uncertainties.add("The candidate may be a legitimate entity that coincidentally uses a similar name.");

        boolean hasImageSignal = evidence.stream()
            .anyMatch(e -> "IMAGE_SIMILARITY".equals(e.getSignalCategory()));
        if (!hasImageSignal) {
            uncertainties.add("Logo/image similarity was not analyzed and may provide additional context.");
        }

        return uncertainties;
    }

    private List<String> buildNextSteps(ThreatDetection detection) {
        List<String> steps = new ArrayList<>();
        steps.add("Review all evidence signals and verify the candidate's identity manually.");

        if (detection.getCandidateType() == ThreatDetection.CandidateType.SOCIAL) {
            steps.add("Visit the social media profile to verify its content and activity.");
            steps.add("Check if the profile has been verified by the platform.");
            steps.add("Compare the profile's posting history and engagement with the official account.");
        } else {
            steps.add("Verify the app in the official store listing.");
            steps.add("Check the developer/publisher credentials and publishing history.");
            steps.add("Test the app in a sandboxed environment if it appears malicious.");
        }

        steps.add("If confirmed malicious, submit a takedown request to the platform.");
        steps.add("Update the investigation status based on your findings.");

        return steps;
    }
}
