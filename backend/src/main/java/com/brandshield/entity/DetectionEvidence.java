package com.brandshield.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "detection_evidence")
public class DetectionEvidence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detection_id", nullable = false)
    private ThreatDetection detection;

    @Column(name = "signal_name", nullable = false, length = 100)
    private String signalName;

    @Column(name = "signal_category", length = 50)
    private String signalCategory;

    @Column(name = "observed_value", columnDefinition = "TEXT")
    private String observedValue;

    @Column(name = "expected_value", columnDefinition = "TEXT")
    private String expectedValue;

    @Column(name = "evidence_explanation", nullable = false, columnDefinition = "TEXT")
    private String evidenceExplanation;

    @Column(name = "score_contribution", nullable = false)
    private Integer scoreContribution = 0;

    @Column(length = 20)
    private String confidence = "MEDIUM";

    public DetectionEvidence() {
    }

    public DetectionEvidence(Long id, ThreatDetection detection, String signalName, String signalCategory,
                             String observedValue, String expectedValue, String evidenceExplanation,
                             Integer scoreContribution, String confidence) {
        this.id = id;
        this.detection = detection;
        this.signalName = signalName;
        this.signalCategory = signalCategory;
        this.observedValue = observedValue;
        this.expectedValue = expectedValue;
        this.evidenceExplanation = evidenceExplanation;
        this.scoreContribution = scoreContribution != null ? scoreContribution : 0;
        this.confidence = confidence != null ? confidence : "MEDIUM";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ThreatDetection getDetection() {
        return detection;
    }

    public void setDetection(ThreatDetection detection) {
        this.detection = detection;
    }

    public String getSignalName() {
        return signalName;
    }

    public void setSignalName(String signalName) {
        this.signalName = signalName;
    }

    public String getSignalCategory() {
        return signalCategory;
    }

    public void setSignalCategory(String signalCategory) {
        this.signalCategory = signalCategory;
    }

    public String getObservedValue() {
        return observedValue;
    }

    public void setObservedValue(String observedValue) {
        this.observedValue = observedValue;
    }

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public String getEvidenceExplanation() {
        return evidenceExplanation;
    }

    public void setEvidenceExplanation(String evidenceExplanation) {
        this.evidenceExplanation = evidenceExplanation;
    }

    public Integer getScoreContribution() {
        return scoreContribution;
    }

    public void setScoreContribution(Integer scoreContribution) {
        this.scoreContribution = scoreContribution;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private ThreatDetection detection;
        private String signalName;
        private String signalCategory;
        private String observedValue;
        private String expectedValue;
        private String evidenceExplanation;
        private Integer scoreContribution = 0;
        private String confidence = "MEDIUM";

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder detection(ThreatDetection detection) {
            this.detection = detection;
            return this;
        }

        public Builder signalName(String signalName) {
            this.signalName = signalName;
            return this;
        }

        public Builder signalCategory(String signalCategory) {
            this.signalCategory = signalCategory;
            return this;
        }

        public Builder observedValue(String observedValue) {
            this.observedValue = observedValue;
            return this;
        }

        public Builder expectedValue(String expectedValue) {
            this.expectedValue = expectedValue;
            return this;
        }

        public Builder evidenceExplanation(String evidenceExplanation) {
            this.evidenceExplanation = evidenceExplanation;
            return this;
        }

        public Builder scoreContribution(Integer scoreContribution) {
            this.scoreContribution = scoreContribution;
            return this;
        }

        public Builder confidence(String confidence) {
            this.confidence = confidence;
            return this;
        }

        public DetectionEvidence build() {
            return new DetectionEvidence(id, detection, signalName, signalCategory, observedValue,
                    expectedValue, evidenceExplanation, scoreContribution, confidence);
        }
    }
}
