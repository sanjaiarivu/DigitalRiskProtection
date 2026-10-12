package com.brandshield.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class ThreatDetectionDetailResponse {
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
    private String aiExplanation;
    private String candidateName;
    private String candidatePlatform;
    private LocalDateTime detectedAt;
    private LocalDateTime updatedAt;
    private List<DetectionEvidenceResponse> evidence;
    private List<InvestigationNoteResponse> notes;
    private List<InvestigationHistoryResponse> history;
    private SocialCandidateResponse socialCandidate;
    private AppCandidateResponse appCandidate;

    public ThreatDetectionDetailResponse() {
    }

    public ThreatDetectionDetailResponse(Long id, Long brandId, String brandName, String detectionType,
                                         String candidateType, int riskScore, String riskLevel,
                                         String status, boolean officialAsset, String summary,
                                         String aiExplanation, String candidateName,
                                         String candidatePlatform, LocalDateTime detectedAt,
                                         LocalDateTime updatedAt, List<DetectionEvidenceResponse> evidence,
                                         List<InvestigationNoteResponse> notes,
                                         List<InvestigationHistoryResponse> history,
                                         SocialCandidateResponse socialCandidate,
                                         AppCandidateResponse appCandidate) {
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
        this.aiExplanation = aiExplanation;
        this.candidateName = candidateName;
        this.candidatePlatform = candidatePlatform;
        this.detectedAt = detectedAt;
        this.updatedAt = updatedAt;
        this.evidence = evidence;
        this.notes = notes;
        this.history = history;
        this.socialCandidate = socialCandidate;
        this.appCandidate = appCandidate;
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

    public String getAiExplanation() {
        return aiExplanation;
    }

    public void setAiExplanation(String aiExplanation) {
        this.aiExplanation = aiExplanation;
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

    public List<DetectionEvidenceResponse> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<DetectionEvidenceResponse> evidence) {
        this.evidence = evidence;
    }

    public List<InvestigationNoteResponse> getNotes() {
        return notes;
    }

    public void setNotes(List<InvestigationNoteResponse> notes) {
        this.notes = notes;
    }

    public List<InvestigationHistoryResponse> getHistory() {
        return history;
    }

    public void setHistory(List<InvestigationHistoryResponse> history) {
        this.history = history;
    }

    public SocialCandidateResponse getSocialCandidate() {
        return socialCandidate;
    }

    public void setSocialCandidate(SocialCandidateResponse socialCandidate) {
        this.socialCandidate = socialCandidate;
    }

    public AppCandidateResponse getAppCandidate() {
        return appCandidate;
    }

    public void setAppCandidate(AppCandidateResponse appCandidate) {
        this.appCandidate = appCandidate;
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
        private String aiExplanation;
        private String candidateName;
        private String candidatePlatform;
        private LocalDateTime detectedAt;
        private LocalDateTime updatedAt;
        private List<DetectionEvidenceResponse> evidence;
        private List<InvestigationNoteResponse> notes;
        private List<InvestigationHistoryResponse> history;
        private SocialCandidateResponse socialCandidate;
        private AppCandidateResponse appCandidate;

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

        public Builder aiExplanation(String aiExplanation) {
            this.aiExplanation = aiExplanation;
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

        public Builder evidence(List<DetectionEvidenceResponse> evidence) {
            this.evidence = evidence;
            return this;
        }

        public Builder notes(List<InvestigationNoteResponse> notes) {
            this.notes = notes;
            return this;
        }

        public Builder history(List<InvestigationHistoryResponse> history) {
            this.history = history;
            return this;
        }

        public Builder socialCandidate(SocialCandidateResponse socialCandidate) {
            this.socialCandidate = socialCandidate;
            return this;
        }

        public Builder appCandidate(AppCandidateResponse appCandidate) {
            this.appCandidate = appCandidate;
            return this;
        }

        public ThreatDetectionDetailResponse build() {
            return new ThreatDetectionDetailResponse(id, brandId, brandName, detectionType, candidateType,
                    riskScore, riskLevel, status, officialAsset, summary, aiExplanation,
                    candidateName, candidatePlatform, detectedAt, updatedAt, evidence,
                    notes, history, socialCandidate, appCandidate);
        }
    }
}
