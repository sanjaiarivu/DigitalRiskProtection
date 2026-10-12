package com.brandshield.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class AiExplanationResponse {
    private String summary;
    private List<String> observedFacts;
    private List<String> reasonsForConcern;
    private List<String> uncertainties;
    private List<String> recommendedNextSteps;
    private LocalDateTime generatedAt;
    private String modelUsed;
    private boolean fallback;

    public AiExplanationResponse() {
    }

    public AiExplanationResponse(String summary, List<String> observedFacts, List<String> reasonsForConcern,
                                 List<String> uncertainties, List<String> recommendedNextSteps,
                                 LocalDateTime generatedAt, String modelUsed, boolean fallback) {
        this.summary = summary;
        this.observedFacts = observedFacts;
        this.reasonsForConcern = reasonsForConcern;
        this.uncertainties = uncertainties;
        this.recommendedNextSteps = recommendedNextSteps;
        this.generatedAt = generatedAt;
        this.modelUsed = modelUsed;
        this.fallback = fallback;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getObservedFacts() {
        return facts(observedFacts);
    }

    private List<String> facts(List<String> list) {
        return list;
    }

    public void setObservedFacts(List<String> observedFacts) {
        this.observedFacts = observedFacts;
    }

    public List<String> getReasonsForConcern() {
        return reasonsForConcern;
    }

    public void setReasonsForConcern(List<String> reasonsForConcern) {
        this.reasonsForConcern = reasonsForConcern;
    }

    public List<String> getUncertainties() {
        return uncertainties;
    }

    public void setUncertainties(List<String> uncertainties) {
        this.uncertainties = uncertainties;
    }

    public List<String> getRecommendedNextSteps() {
        return recommendedNextSteps;
    }

    public void setRecommendedNextSteps(List<String> recommendedNextSteps) {
        this.recommendedNextSteps = recommendedNextSteps;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getModelUsed() {
        return modelUsed;
    }

    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }

    public boolean isFallback() {
        return fallback;
    }

    public boolean getFallback() {
        return fallback;
    }

    public void setFallback(boolean fallback) {
        this.fallback = fallback;
    }

    public static class Builder {
        private String summary;
        private List<String> observedFacts;
        private List<String> reasonsForConcern;
        private List<String> uncertainties;
        private List<String> recommendedNextSteps;
        private LocalDateTime generatedAt;
        private String modelUsed;
        private boolean fallback;

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder facts(List<String> observedFacts) {
            this.observedFacts = observedFacts;
            return this;
        }

        public Builder observedFacts(List<String> observedFacts) {
            this.observedFacts = observedFacts;
            return this;
        }

        public Builder reasonsForConcern(List<String> reasonsForConcern) {
            this.reasonsForConcern = reasonsForConcern;
            return this;
        }

        public Builder uncertainties(List<String> uncertainties) {
            this.uncertainties = uncertainties;
            return this;
        }

        public Builder recommendedNextSteps(List<String> recommendedNextSteps) {
            this.recommendedNextSteps = recommendedNextSteps;
            return this;
        }

        public Builder generatedAt(LocalDateTime generatedAt) {
            this.generatedAt = generatedAt;
            return this;
        }

        public Builder modelUsed(String modelUsed) {
            this.modelUsed = modelUsed;
            return this;
        }

        public Builder fallback(boolean fallback) {
            this.fallback = fallback;
            return this;
        }

        public AiExplanationResponse build() {
            return new AiExplanationResponse(summary, observedFacts, reasonsForConcern,
                    uncertainties, recommendedNextSteps, generatedAt, modelUsed, fallback);
        }
    }
}
