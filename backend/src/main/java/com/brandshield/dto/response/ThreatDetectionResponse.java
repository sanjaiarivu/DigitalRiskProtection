package com.brandshield.dto.response;

import java.time.LocalDateTime;

public class ThreatDetectionResponse {
    private Long id;
    private Long brandId;
    private String brandName;
    private String detectionType;
    private String candidateType;
    private int riskScore;
    private String riskLevel;
    private String status;
    private boolean officialAsset;
    private String summary;
    private String candidateName;
    private String candidatePlatform;
    private LocalDateTime detectedAt;
    private LocalDateTime updatedAt;

    public ThreatDetectionResponse() {
    }

    public ThreatDetectionResponse(Long id, Long brandId, String brandName, String detectionType,
                                   String candidateType, int riskScore, String riskLevel,
                                   String status, boolean officialAsset, String summary,
                                   String candidateName, String candidatePlatform,
                                   LocalDateTime detectedAt, LocalDateTime updatedAt) {
        this.id = id;
        this.brandId = brandId;
        this.brandName = brandName;
        this.detectionType = detectionType;
        this.candidateType = candidateType;
        this.riskScore = riskScore;
        this.riskLevel = riskLevel;
        this.status = status;
        this.officialAsset = officialAsset;
        this.summary = summary;
        this.candidateName = candidateName;
        this.candidatePlatform = candidatePlatform;
        this.detectedAt = detectedAt;
        this.updatedAt = updatedAt;
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

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDetectionType() {
        return detectionType;
    }

    public void setDetectionType(String detectionType) {
        this.detectionType = detectionType;
    }

    public String getCandidateType() {
        return candidateType;
    }

    public void setCandidateType(String candidateType) {
        this.candidateType = candidateType;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOfficialAsset() {
        return officialAsset;
    }

    public boolean getOfficialAsset() {
        return officialAsset;
    }

    public void setOfficialAsset(boolean officialAsset) {
        this.officialAsset = officialAsset;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidatePlatform() {
        return candidatePlatform;
    }

    public void setCandidatePlatform(String candidatePlatform) {
        this.candidatePlatform = candidatePlatform;
    }

    public LocalDateTime getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(LocalDateTime detectedAt) {
        this.detectedAt = detectedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class Builder {
        private Long id;
        private Long brandId;
        private String brandName;
        private String detectionType;
        private String candidateType;
        private int riskScore;
        private String riskLevel;
        private String status;
        private boolean officialAsset;
        private String summary;
        private String candidateName;
        private String candidatePlatform;
        private LocalDateTime detectedAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder brandId(Long brandId) {
            this.brandId = brandId;
            return this;
        }

        public Builder brandName(String brandName) {
            this.brandName = brandName;
            return this;
        }

        public Builder detectionType(String detectionType) {
            this.detectionType = detectionType;
            return this;
        }

        public Builder candidateType(String candidateType) {
            this.candidateType = candidateType;
            return this;
        }

        public Builder riskScore(int riskScore) {
            this.riskScore = riskScore;
            return this;
        }

        public Builder riskLevel(String riskLevel) {
            this.riskLevel = riskLevel;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder officialAsset(boolean officialAsset) {
            this.officialAsset = officialAsset;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder candidateName(String candidateName) {
            this.candidateName = candidateName;
            return this;
        }

        public Builder candidatePlatform(String candidatePlatform) {
            this.candidatePlatform = candidatePlatform;
            return this;
        }

        public Builder detectedAt(LocalDateTime detectedAt) {
            this.detectedAt = detectedAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ThreatDetectionResponse build() {
            return new ThreatDetectionResponse(id, brandId, brandName, detectionType, candidateType,
                    riskScore, riskLevel, status, officialAsset, summary,
                    candidateName, candidatePlatform, detectedAt, updatedAt);
        }
    }
}
