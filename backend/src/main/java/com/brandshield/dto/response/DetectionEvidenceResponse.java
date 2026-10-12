package com.brandshield.dto.response;

public class DetectionEvidenceResponse {
    private Long id;
    private String signalName;
    private String signalCategory;
    private String observedValue;
    private String expectedValue;
    private String evidenceExplanation;
    private int scoreContribution;
    private String confidence;

    public DetectionEvidenceResponse() {
    }

    public DetectionEvidenceResponse(Long id, String signalName, String signalCategory,
                                     String observedValue, String expectedValue,
                                     String evidenceExplanation, int scoreContribution,
                                     String confidence) {
        this.id = id;
        this.signalName = signalName;
        this.signalCategory = signalCategory;
        this.observedValue = observedValue;
        this.expectedValue = expectedValue;
        this.evidenceExplanation = evidenceExplanation;
        this.scoreContribution = scoreContribution;
        this.confidence = confidence;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getScoreContribution() {
        return scoreContribution;
    }

    public void setScoreContribution(int scoreContribution) {
        this.scoreContribution = scoreContribution;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public static class Builder {
        private Long id;
        private String signalName;
        private String signalCategory;
        private String observedValue;
        private String expectedValue;
        private String evidenceExplanation;
        private int scoreContribution;
        private String confidence;

        public Builder id(Long id) {
            this.id = id;
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

        public Builder scoreContribution(int scoreContribution) {
            this.scoreContribution = scoreContribution;
            return this;
        }

        public Builder confidence(String confidence) {
            this.confidence = confidence;
            return this;
        }

        public DetectionEvidenceResponse build() {
            return new DetectionEvidenceResponse(id, signalName, signalCategory, observedValue,
                    expectedValue, evidenceExplanation, scoreContribution, confidence);
        }
    }
}
