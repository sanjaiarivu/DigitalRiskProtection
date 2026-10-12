package com.brandshield.dto.response;

import java.time.LocalDateTime;

public class InvestigationNoteResponse {
    private Long id;
    private String author;
    private String noteText;
    private LocalDateTime createdAt;

    public InvestigationNoteResponse() {
    }

    public InvestigationNoteResponse(Long id, String author, String noteText, LocalDateTime createdAt) {
        this.id = id;
        this.author = author;
        this.noteText = noteText;
        this.createdAt = createdAt;
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

    public static class Builder {
        private Long id;
        private String author;
        private String noteText;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
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

        public InvestigationNoteResponse build() {
            return new InvestigationNoteResponse(id, author, noteText, createdAt);
        }
    }
}
