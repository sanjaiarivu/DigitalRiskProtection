package com.brandshield.detection;

import com.brandshield.entity.ThreatDetection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RiskScorerTest {

    private RiskScorer scorer;

    @BeforeEach
    void setUp() {
        scorer = new RiskScorer();
    }

    @Test
    @DisplayName("Verified official asset must receive score 0 and LOW risk level")
    void testOfficialAssetScoreZero() {
        List<DetectionSignal> signals = List.of(
                new DetectionSignal(
                        "NAME_SIMILARITY",
                        DetectionSignal.Category.NAME_SIMILARITY.name(),
                        "techvault",
                        "techvault",
                        "Name is identical",
                        30,
                        DetectionSignal.Confidence.HIGH
                )
        );

        RiskScorer.RiskAssessment assessment = scorer.calculateRiskScore(signals, true);
        assertEquals(0, assessment.score());
        assertEquals(ThreatDetection.RiskLevel.LOW, assessment.riskLevel());
        assertTrue(assessment.explanation().contains("official asset"));
    }

    @Test
    @DisplayName("Empty signals must produce score 0 and LOW risk level")
    void testEmptySignals() {
        RiskScorer.RiskAssessment assessment = scorer.calculateRiskScore(List.of(), false);
        assertEquals(0, assessment.score());
        assertEquals(ThreatDetection.RiskLevel.LOW, assessment.riskLevel());
    }

    @Test
    @DisplayName("Multiple high-severity signals should compound into CRITICAL risk level")
    void testCompoundingThreatSignals() {
        List<DetectionSignal> signals = List.of(
                new DetectionSignal(
                        "IDENTIFIER_CONFLICT",
                        DetectionSignal.Category.IDENTIFIER_CONFLICT.name(),
                        "com.techvau1t.app",
                        "com.techvault.app",
                        "Conflicting package ID",
                        30,
                        DetectionSignal.Confidence.HIGH
                ),
                new DetectionSignal(
                        "PUBLISHER_MISMATCH",
                        DetectionSignal.Category.PUBLISHER_MISMATCH.name(),
                        "Rogue Developers Ltd",
                        "TechVault Inc",
                        "Publisher is not verified",
                        25,
                        DetectionSignal.Confidence.HIGH
                ),
                new DetectionSignal(
                        "CHAR_SUBSTITUTION",
                        DetectionSignal.Category.CHARACTER_SUBSTITUTION.name(),
                        "techvau1t",
                        "techvault",
                        "Character substitution 1 for l",
                        20,
                        DetectionSignal.Confidence.HIGH
                )
        );

        RiskScorer.RiskAssessment assessment = scorer.calculateRiskScore(signals, false);
        assertTrue(assessment.score() >= 76, "Compounded score should exceed 75");
        assertEquals(ThreatDetection.RiskLevel.CRITICAL, assessment.riskLevel());
    }

    @Test
    @DisplayName("Risk score must be capped at 100")
    void testScoreCappedAt100() {
        List<DetectionSignal> signals = List.of(
                new DetectionSignal("SIG1", DetectionSignal.Category.IDENTIFIER_CONFLICT.name(), "v", "e", "exp", 30, DetectionSignal.Confidence.HIGH),
                new DetectionSignal("SIG2", DetectionSignal.Category.PUBLISHER_MISMATCH.name(), "v", "e", "exp", 30, DetectionSignal.Confidence.HIGH),
                new DetectionSignal("SIG3", DetectionSignal.Category.CHARACTER_SUBSTITUTION.name(), "v", "e", "exp", 30, DetectionSignal.Confidence.HIGH),
                new DetectionSignal("SIG4", DetectionSignal.Category.SUSPICIOUS_LINK.name(), "v", "e", "exp", 30, DetectionSignal.Confidence.HIGH)
        );

        RiskScorer.RiskAssessment assessment = scorer.calculateRiskScore(signals, false);
        assertEquals(100, assessment.score());
        assertEquals(ThreatDetection.RiskLevel.CRITICAL, assessment.riskLevel());
    }

    @Test
    @DisplayName("Single category signals must not be double counted")
    void testNoDoubleCountingInCategory() {
        List<DetectionSignal> signals = List.of(
                new DetectionSignal("SIM1", DetectionSignal.Category.NAME_SIMILARITY.name(), "a", "b", "exp1", 10, DetectionSignal.Confidence.LOW),
                new DetectionSignal("SIM2", DetectionSignal.Category.NAME_SIMILARITY.name(), "a", "b", "exp2", 20, DetectionSignal.Confidence.HIGH)
        );

        RiskScorer.RiskAssessment assessment = scorer.calculateRiskScore(signals, false);
        // Only highest (20 * 1.0) = 20, not 10 + 20
        assertEquals(20, assessment.score());
        assertEquals(ThreatDetection.RiskLevel.LOW, assessment.riskLevel());
    }
}
