package com.brandshield.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "investigation_notes")
public class InvestigationNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detection_id", nullable = false)
    private ThreatDetection detection;

    @Column
    private String author = "analyst";

    @Column(name = "note_text", nullable = false, columnDefinition = "TEXT")
    private String noteText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public InvestigationNote() {
    }

    public InvestigationNote(Long id, ThreatDetection detection, String author, String noteText, LocalDateTime createdAt) {
        this.id = id;
        this.detection = detection;
        this.author = author != null ? author : "analyst";
        this.noteText = noteText;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private ThreatDetection detection;
        private String author = "analyst";
        private String noteText;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder detection(ThreatDetection detection) {
            this.detection = detection;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder noteText(String noteText) {
            this.noteText = noteText;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public InvestigationNote build() {
            return new InvestigationNote(id, detection, author, noteText, createdAt);
        }
    }
}
