package com.brandshield.detection;

import com.brandshield.entity.ThreatDetection;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Calculates risk scores from detection signals using configurable category weights.
 *
 * Scoring methodology:
 * - Each signal has a base score contribution (0-30) assigned by the analyzer.
 * - The contribution is multiplied by a category weight that reflects the signal's
 *   reliability as an impersonation indicator.
 * - The total is capped at 100.
 * - Risk levels are assigned based on fixed thresholds.
 *
 * Weight rationale:
 * - IDENTIFIER_CONFLICT (1.5x): A conflicting package ID or account ID is the
 *   strongest impersonation indicator because legitimate apps don't accidentally
 *   use another company's registered identifier.
 * - PUBLISHER_MISMATCH (1.3x): Wrong publisher is a strong signal for app stores.
 * - CHARACTER_SUBSTITUTION (1.2x): Deliberate look-alike characters (e.g., 1→l)
 *   suggest intentional impersonation rather than coincidence.
 * - SUSPICIOUS_LINK (1.1x): External links to phishing domains are concerning.
 * - NAME_SIMILARITY (1.0x): Baseline weight; similar names are common but not
 *   conclusive on their own.
 * - AFFIX_PATTERN (1.0x): Added prefixes/suffixes are suspicious but could be
 *   fan accounts or customer service.
 * - DESCRIPTION_SIMILARITY (0.8x): Similar descriptions have higher false positive
 *   rates since companies in the same industry use similar language.
 * - METADATA_ANOMALY (0.7x): Metadata issues are the weakest signal.
 * - IMAGE_SIMILARITY (1.1x): Similar logos are suspicious but require human verification.
 */
@Component
public class RiskScorer {

    private static final Map<String, Double> CATEGORY_WEIGHTS = Map.of(
        DetectionSignal.Category.NAME_SIMILARITY.name(), 1.0,
        DetectionSignal.Category.CHARACTER_SUBSTITUTION.name(), 1.2,
        DetectionSignal.Category.AFFIX_PATTERN.name(), 1.0,
        DetectionSignal.Category.PUBLISHER_MISMATCH.name(), 1.3,
        DetectionSignal.Category.SUSPICIOUS_LINK.name(), 1.1,
        DetectionSignal.Category.DESCRIPTION_SIMILARITY.name(), 0.8,
        DetectionSignal.Category.IDENTIFIER_CONFLICT.name(), 1.5,
        DetectionSignal.Category.METADATA_ANOMALY.name(), 0.7,
        DetectionSignal.Category.IMAGE_SIMILARITY.name(), 1.1
    );

    /**
     * Calculates risk score from detection signals.
     * If the candidate is a verified official asset, returns score 0 / LOW.
     */
    public RiskAssessment calculateRiskScore(List<DetectionSignal> signals, boolean isOfficialAsset) {
        if (isOfficialAsset) {
            return new RiskAssessment(0, ThreatDetection.RiskLevel.LOW, signals, List.of(),
                "Candidate matches a registered official asset. No risk.");
        }

        if (signals == null || signals.isEmpty()) {
            return new RiskAssessment(0, ThreatDetection.RiskLevel.LOW, List.of(), List.of(),
                "No signals detected.");
        }

        // Track which categories contributed to avoid double-counting.
        // Within a category, we take the highest-scoring signal only.
        Map<String, DetectionSignal> bestPerCategory = new LinkedHashMap<>();
        for (DetectionSignal signal : signals) {
            String category = signal.signalCategory();
            DetectionSignal existing = bestPerCategory.get(category);
            if (existing == null || signal.scoreContribution() > existing.scoreContribution()) {
                bestPerCategory.put(category, signal);
            }
        }

        double rawScore = 0;
        for (Map.Entry<String, DetectionSignal> entry : bestPerCategory.entrySet()) {
            double weight = CATEGORY_WEIGHTS.getOrDefault(entry.getKey(), 1.0);
            rawScore += entry.getValue().scoreContribution() * weight;
        }

        int finalScore = Math.min(100, (int) Math.round(rawScore));
        ThreatDetection.RiskLevel level = determineRiskLevel(finalScore);

        String explanation = String.format(
            "Risk score %d calculated from %d signal(s) across %d category(ies).",
            finalScore, signals.size(), bestPerCategory.size()
        );

        return new RiskAssessment(finalScore, level, signals, List.of(), explanation);
    }

    private ThreatDetection.RiskLevel determineRiskLevel(int score) {
        if (score >= 76) return ThreatDetection.RiskLevel.CRITICAL;
        if (score >= 51) return ThreatDetection.RiskLevel.HIGH;
        if (score >= 26) return ThreatDetection.RiskLevel.MEDIUM;
        return ThreatDetection.RiskLevel.LOW;
    }

    public record RiskAssessment(
        int score,
        ThreatDetection.RiskLevel riskLevel,
        List<DetectionSignal> signals,
        List<String> unavailableSignals,
        String explanation
    ) {}
}
