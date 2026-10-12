package com.brandshield.detection;

/**
 * Represents a single detection signal produced during candidate analysis.
 * Each signal contributes to the overall risk score with a documented explanation.
 */
public record DetectionSignal(
    String signalName,
    String signalCategory,
    String observedValue,
    String expectedValue,
    String explanation,
    int scoreContribution,
    Confidence confidence
) {
    public enum Confidence {
        HIGH, MEDIUM, LOW
    }

    public enum Category {
        NAME_SIMILARITY,
        CHARACTER_SUBSTITUTION,
        AFFIX_PATTERN,
        PUBLISHER_MISMATCH,
        SUSPICIOUS_LINK,
        DESCRIPTION_SIMILARITY,
        IDENTIFIER_CONFLICT,
        METADATA_ANOMALY,
        IMAGE_SIMILARITY
    }
}
