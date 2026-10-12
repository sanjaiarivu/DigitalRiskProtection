package com.brandshield.dto.response;

import java.util.List;
import java.util.Map;

public class DashboardSummaryResponse {
    private long totalBrands;
    private long totalCandidates;
    private long totalDetections;
    private long newDetections;
    private long highRiskDetections;
    private long criticalDetections;
    private Map<String, Long> riskDistribution;
    private Map<String, Long> detectionsByType;
    private List<ThreatDetectionResponse> recentDetections;

    public DashboardSummaryResponse() {
    }

    public DashboardSummaryResponse(long totalBrands, long totalCandidates, long totalDetections,
                                    long newDetections, long highRiskDetections, long criticalDetections,
                                    Map<String, Long> riskDistribution, Map<String, Long> detectionsByType,
                                    List<ThreatDetectionResponse> recentDetections) {
        this.totalBrands = totalBrands;
        this.totalCandidates = totalCandidates;
        this.totalDetections = totalDetections;
        this.newDetections = newDetections;
        this.highRiskDetections = highRiskDetections;
        this.criticalDetections = criticalDetections;
        this.riskDistribution = riskDistribution;
        this.detectionsByType = detectionsByType;
        this.recentDetections = recentDetections;
    }

    public static Builder builder() {
        return new Builder();
    }

    public long getTotalBrands() {
        return totalBrands;
    }

    public void setTotalBrands(long totalBrands) {
        this.totalBrands = totalBrands;
    }

    public long getTotalCandidates() {
        return totalCandidates;
    }

    public void setTotalCandidates(long totalCandidates) {
        this.totalCandidates = totalCandidates;
    }

    public long getTotalDetections() {
        return totalDetections;
    }

    public void setTotalDetections(long totalDetections) {
        this.totalDetections = totalDetections;
    }

    public long getNewDetections() {
        return newDetections;
    }

    public void setNewDetections(long newDetections) {
        this.newDetections = newDetections;
    }

    public long getHighRiskDetections() {
        return highRiskDetections;
    }

    public void setHighRiskDetections(long highRiskDetections) {
        this.highRiskDetections = highRiskDetections;
    }

    public long getCriticalDetections() {
        return criticalDetections;
    }

    public void setCriticalDetections(long criticalDetections) {
        this.criticalDetections = criticalDetections;
    }

    public Map<String, Long> getRiskDistribution() {
        return riskDistribution;
    }

    public void setRiskDistribution(Map<String, Long> riskDistribution) {
        this.riskDistribution = riskDistribution;
    }

    public Map<String, Long> getDetectionsByType() {
        return detectionsByType;
    }

    public void setDetectionsByType(Map<String, Long> detectionsByType) {
        this.detectionsByType = detectionsByType;
    }

    public List<ThreatDetectionResponse> getRecentDetections() {
        return recentDetections;
    }

    public void setRecentDetections(List<ThreatDetectionResponse> recentDetections) {
        this.recentDetections = recentDetections;
    }

    public static class Builder {
        private long totalBrands;
        private long totalCandidates;
        private long totalDetections;
        private long newDetections;
        private long highRiskDetections;
        private long criticalDetections;
        private Map<String, Long> riskDistribution;
        private Map<String, Long> detectionsByType;
        private List<ThreatDetectionResponse> recentDetections;

        public Builder totalBrands(long totalBrands) {
            this.totalBrands = totalBrands;
            return this;
        }

        public Builder totalCandidates(long totalCandidates) {
            this.totalCandidates = totalCandidates;
            return this;
        }

        public Builder totalDetections(long totalDetections) {
            this.totalDetections = totalDetections;
            return this;
        }

        public Builder newDetections(long newDetections) {
            this.newDetections = newDetections;
            return this;
        }

        public Builder highRiskDetections(long highRiskDetections) {
            this.highRiskDetections = highRiskDetections;
            return this;
        }

        public Builder criticalDetections(long criticalDetections) {
            this.criticalDetections = criticalDetections;
            return this;
        }

        public Builder riskDistribution(Map<String, Long> riskDistribution) {
            this.riskDistribution = riskDistribution;
            return this;
        }

        public Builder detectionsByType(Map<String, Long> detectionsByType) {
            this.detectionsByType = detectionsByType;
            return this;
        }

        public Builder recentDetections(List<ThreatDetectionResponse> recentDetections) {
            this.recentDetections = recentDetections;
            return this;
        }

        public DashboardSummaryResponse build() {
            return new DashboardSummaryResponse(totalBrands, totalCandidates, totalDetections,
                    newDetections, highRiskDetections, criticalDetections,
                    riskDistribution, detectionsByType, recentDetections);
        }
    }
}
