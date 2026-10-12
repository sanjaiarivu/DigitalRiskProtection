package com.brandshield.dto.response;

import java.time.LocalDateTime;

public class SocialCandidateResponse {
    private Long id;
    private String platform;
    private String username;
    private String displayName;
    private String accountId;
    private String profileUrl;
    private String description;
    private String avatarUrl;
    private String externalLinks;
    private Integer followerCount;
    private boolean synthetic;
    private String source;
    private LocalDateTime importedAt;

    public SocialCandidateResponse() {
    }

    public SocialCandidateResponse(Long id, String platform, String username, String displayName,
                                   String accountId, String profileUrl, String description,
                                   String avatarUrl, String externalLinks, Integer followerCount,
                                   boolean synthetic, String source, LocalDateTime importedAt) {
        this.id = id;
        this.platform = platform;
        this.username = username;
        this.displayName = displayName;
        this.accountId = accountId;
        this.profileUrl = profileUrl;
        this.description = description;
        this.avatarUrl = avatarUrl;
        this.externalLinks = externalLinks;
        this.followerCount = followerCount;
        this.synthetic = synthetic;
        this.source = source;
        this.importedAt = importedAt;
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

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getExternalLinks() {
        return externalLinks;
    }

    public void setExternalLinks(String externalLinks) {
        this.externalLinks = externalLinks;
    }

    public Integer getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public boolean isSynthetic() {
        return synthetic;
    }

    public boolean getSynthetic() {
        return synthetic;
    }

    public void setSynthetic(boolean synthetic) {
        this.synthetic = synthetic;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDateTime getImportedAt() {
        return importedAt;
    }

    public void setImportedAt(LocalDateTime importedAt) {
        this.importedAt = importedAt;
    }

    public static class Builder {
        private Long id;
        private String platform;
        private String username;
        private String displayName;
        private String accountId;
        private String profileUrl;
        private String description;
        private String avatarUrl;
        private String externalLinks;
        private Integer followerCount;
        private boolean synthetic;
        private String source;
        private LocalDateTime importedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder platform(String platform) {
            this.platform = platform;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder profileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public Builder externalLinks(String externalLinks) {
            this.externalLinks = externalLinks;
            return this;
        }

        public Builder followerCount(Integer followerCount) {
            this.followerCount = followerCount;
            return this;
        }

        public Builder synthetic(boolean synthetic) {
            this.synthetic = synthetic;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder importedAt(LocalDateTime importedAt) {
            this.importedAt = importedAt;
            return this;
        }

        public SocialCandidateResponse build() {
            return new SocialCandidateResponse(id, platform, username, displayName, accountId,
                    profileUrl, description, avatarUrl, externalLinks, followerCount,
                    synthetic, source, importedAt);
        }
    }
}
