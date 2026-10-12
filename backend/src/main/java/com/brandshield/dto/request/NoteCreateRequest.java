package com.brandshield.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NoteCreateRequest {

    @NotBlank(message = "Note text is required")
    @Size(max = 5000, message = "Note text must be at most 5000 characters")
    private String noteText;

    private String author;

    public NoteCreateRequest() {
    }

    public NoteCreateRequest(String noteText, String author) {
        this.noteText = noteText;
        this.author = author;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String noteText;
        private String author;

        public Builder noteText(String noteText) {
            this.noteText = noteText;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public NoteCreateRequest build() {
            return new NoteCreateRequest(noteText, author);
        }
    }
}
