package com.brandshield.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "threat_detections")
public class ThreatDetection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "detection_type", nullable = false, length = 50)
    private DetectionType detectionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "candidate_type", nullable = false, length = 50)
    private CandidateType candidateType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_candidate_id")
    private SocialCandidate socialCandidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_candidate_id")
    private AppCandidate appCandidate;

    @Column(name = "risk_score", nullable = false)
    private Integer riskScore = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 20)
    private RiskLevel riskLevel = RiskLevel.LOW;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DetectionStatus status = DetectionStatus.NEW;

    @Column(name = "is_official_asset")
    private Boolean isOfficialAsset = false;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "ai_explanation", columnDefinition = "TEXT")
    private String aiExplanation;

    @Column(name = "detected_at")
    private LocalDateTime detectedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "detection", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetectionEvidence> evidence = new ArrayList<>();

    @OneToMany(mappedBy = "detection", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<InvestigationNote> notes = new ArrayList<>();

    @OneToMany(mappedBy = "detection", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<InvestigationHistory> history = new ArrayList<>();

    public ThreatDetection() {
    }

    public ThreatDetection(Long id, Brand brand, DetectionType detectionType, CandidateType candidateType,
                           SocialCandidate socialCandidate, AppCandidate appCandidate, Integer riskScore,
                           RiskLevel riskLevel, DetectionStatus status, Boolean isOfficialAsset,
                           String summary, String aiExplanation, LocalDateTime detectedAt,
                           LocalDateTime updatedAt, List<DetectionEvidence> evidence,
                           List<InvestigationNote> notes, List<InvestigationHistory> history) {
        this.id = id;
        this.brand = brand;
        this.detectionType = detectionType;
        this.candidateType = candidateType;
        this.socialCandidate = socialCandidate;
        this.appCandidate = appCandidate;
        this.riskScore = riskScore != null ? riskScore : 0;
        this.riskLevel = riskLevel != null ? riskLevel : RiskLevel.LOW;
        this.status = status != null ? status : DetectionStatus.NEW;
        this.isOfficialAsset = isOfficialAsset != null ? isOfficialAsset : false;
        this.summary = summary;
        this.aiExplanation = aiExplanation;
        this.detectedAt = detectedAt;
        this.updatedAt = updatedAt;
        this.evidence = evidence != null ? evidence : new ArrayList<>();
        this.notes = notes != null ? notes : new ArrayList<>();
        this.history = history != null ? history : new ArrayList<>();
    }

    @PrePersist
    protected void onCreate() {
        detectedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addEvidence(DetectionEvidence e) {
        evidence.add(e);
        e.setDetection(this);
    }

    public enum DetectionType {
        BRAND_IMPERSONATION, LOOK_ALIKE, PHISHING_INDICATOR, PUBLISHER_MISMATCH, SUSPICIOUS_CONTENT
    }

    public enum CandidateType {
        SOCIAL, APP
    }

    public enum RiskLevel {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum DetectionStatus {
        NEW, UNDER_INVESTIGATION, LEGITIMATE, DISMISSED
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public DetectionType getDetectionType() {
        return detectionType;
    }

    public void setDetectionType(DetectionType detectionType) {
        this.detectionType = detectionType;
    }

    public CandidateType getCandidateType() {
        return candidateType;
    }

    public void setCandidateType(CandidateType candidateType) {
        this.candidateType = candidateType;
    }

    public SocialCandidate getSocialCandidate() {
        return socialCandidate;
    }

    public void setSocialCandidate(SocialCandidate socialCandidate) {
        this.socialCandidate = socialCandidate;
    }

    public AppCandidate getAppCandidate() {
        return appCandidate;
    }

    public void setAppCandidate(AppCandidate appCandidate) {
        this.appCandidate = appCandidate;
    }

    public Integer getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public DetectionStatus getStatus() {
        return status;
    }

    public void setStatus(DetectionStatus status) {
        this.status = status;
    }

    public Boolean getIsOfficialAsset() {
        return isOfficialAsset;
    }

    public void setIsOfficialAsset(Boolean isOfficialAsset) {
        this.isOfficialAsset = isOfficialAsset;
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

    public List<DetectionEvidence> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<DetectionEvidence> evidence) {
        this.evidence = evidence;
    }

    public List<InvestigationNote> getNotes() {
        return notes;
    }

    public void setNotes(List<InvestigationNote> notes) {
        this.notes = notes;
    }

    public List<InvestigationHistory> getHistory() {
        return history;
    }

    public void setHistory(List<InvestigationHistory> history) {
        this.history = history;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Brand brand;
        private DetectionType detectionType;
        private CandidateType candidateType;
        private SocialCandidate socialCandidate;
        private AppCandidate appCandidate;
        private Integer riskScore = 0;
        private RiskLevel riskLevel = RiskLevel.LOW;
        private DetectionStatus status = DetectionStatus.NEW;
        private Boolean isOfficialAsset = false;
        private String summary;
        private String aiExplanation;
        private LocalDateTime detectedAt;
        private LocalDateTime updatedAt;
        private List<DetectionEvidence> evidence = new ArrayList<>();
        private List<InvestigationNote> notes = new ArrayList<>();
        private List<InvestigationHistory> history = new ArrayList<>();

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder brand(Brand brand) {
            this.brand = brand;
            return this;
        }

        public Builder detectionType(DetectionType detectionType) {
            this.detectionType = detectionType;
            return this;
        }

        public Builder candidateType(CandidateType candidateType) {
            this.candidateType = candidateType;
            return this;
        }

        public Builder socialCandidate(SocialCandidate socialCandidate) {
            this.socialCandidate = socialCandidate;
            return this;
        }

        public Builder appCandidate(AppCandidate appCandidate) {
            this.appCandidate = appCandidate;
            return this;
        }

        public Builder riskScore(Integer riskScore) {
            this.riskScore = riskScore;
            return this;
        }

        public Builder riskLevel(RiskLevel riskLevel) {
            this.riskLevel = riskLevel;
            return this;
        }

        public Builder status(DetectionStatus status) {
            this.status = status;
            return this;
        }

        public Builder isOfficialAsset(Boolean isOfficialAsset) {
            this.isOfficialAsset = isOfficialAsset;
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

        public Builder detectedAt(LocalDateTime detectedAt) {
            this.detectedAt = detectedAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder evidence(List<DetectionEvidence> evidence) {
            this.evidence = evidence;
            return this;
        }

        public Builder notes(List<InvestigationNote> notes) {
            this.notes = notes;
            return this;
        }

        public Builder history(List<InvestigationHistory> history) {
            this.history = history;
            return this;
        }

        public ThreatDetection build() {
            return new ThreatDetection(id, brand, detectionType, candidateType, socialCandidate,
                    appCandidate, riskScore, riskLevel, status, isOfficialAsset, summary,
                    aiExplanation, detectedAt, updatedAt, evidence, notes, history);
        }
    }
}
