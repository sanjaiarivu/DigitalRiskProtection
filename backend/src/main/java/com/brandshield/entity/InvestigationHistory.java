package com.brandshield.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "investigation_history")
public class InvestigationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detection_id", nullable = false)
    private ThreatDetection detection;

    @Column(name = "previous_status", length = 30)
    private String previousStatus;

    @Column(name = "new_status", nullable = false, length = 30)
    private String newStatus;

    @Column(name = "changed_by")
    private String changedBy = "analyst";

    @Column(name = "change_reason", columnDefinition = "TEXT")
    private String changeReason;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    public InvestigationHistory() {
    }

    public InvestigationHistory(Long id, ThreatDetection detection, String previousStatus,
                                String newStatus, String changedBy, String changeReason, LocalDateTime changedAt) {
        this.id = id;
        this.detection = detection;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy != null ? changedBy : "analyst";
        this.changeReason = changeReason;
        this.changedAt = changedAt;
    }

    @PrePersist
    protected void onCreate() {
        changedAt = LocalDateTime.now();
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

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private ThreatDetection detection;
        private String previousStatus;
        private String newStatus;
        private String changedBy = "analyst";
        private String changeReason;
        private LocalDateTime changedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder detection(ThreatDetection detection) {
            this.detection = detection;
            return this;
        }

        public Builder previousStatus(String previousStatus) {
            this.previousStatus = previousStatus;
            return this;
        }

        public Builder newStatus(String newStatus) {
            this.newStatus = newStatus;
            return this;
        }

        public Builder changedBy(String changedBy) {
            this.changedBy = changedBy;
            return this;
        }

        public Builder changeReason(String changeReason) {
            this.changeReason = changeReason;
            return this;
        }

        public Builder changedAt(LocalDateTime changedAt) {
            this.changedAt = changedAt;
            return this;
        }

        public InvestigationHistory build() {
            return new InvestigationHistory(id, detection, previousStatus, newStatus, changedBy, changeReason, changedAt);
        }
    }
}
