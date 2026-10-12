package com.brandshield.dto.response;

import java.time.LocalDateTime;

public class InvestigationHistoryResponse {
    private Long id;
    private String previousStatus;
    private String newStatus;
    private String changedBy;
    private String changeReason;
    private LocalDateTime changedAt;

    public InvestigationHistoryResponse() {
    }

    public InvestigationHistoryResponse(Long id, String previousStatus, String newStatus,
                                        String changedBy, String changeReason,
                                        LocalDateTime changedAt) {
        this.id = id;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
        this.changeReason = changeReason;
        this.changedAt = changedAt;
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

    public static class Builder {
        private Long id;
        private String previousStatus;
        private String newStatus;
        private String changedBy;
        private String changeReason;
        private LocalDateTime changedAt;

        public Builder id(Long id) {
            this.id = id;
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

        public InvestigationHistoryResponse build() {
            return new InvestigationHistoryResponse(id, previousStatus, newStatus, changedBy, changeReason, changedAt);
        }
    }
}
