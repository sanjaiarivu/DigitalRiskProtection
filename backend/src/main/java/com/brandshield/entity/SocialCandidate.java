package com.brandshield.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "social_candidates")
public class SocialCandidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String platform;

    @Column
    private String username;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "profile_url", length = 500)
    private String profileUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "external_links", columnDefinition = "TEXT")
    private String externalLinks;

    @Column(name = "follower_count")
    private Integer followerCount;

    @Column(name = "is_synthetic")
    private Boolean isSynthetic = false;

    @Column(length = 100)
    private String source = "IMPORT";

    @Column(name = "imported_at")
    private LocalDateTime importedAt;

    public SocialCandidate() {
    }

    public SocialCandidate(Long id, String platform, String username, String displayName, String accountId,
                           String profileUrl, String description, String avatarUrl, String externalLinks,
                           Integer followerCount, Boolean isSynthetic, String source, LocalDateTime importedAt) {
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
        this.isSynthetic = isSynthetic != null ? isSynthetic : false;
        this.source = source != null ? source : "IMPORT";
        this.importedAt = importedAt;
    }

    @PrePersist
    protected void onCreate() {
        importedAt = LocalDateTime.now();
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

    public Boolean getIsSynthetic() {
        return isSynthetic;
    }

    public void setIsSynthetic(Boolean isSynthetic) {
        this.isSynthetic = isSynthetic;
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

    public static Builder builder() {
        return new Builder();
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
        private Boolean isSynthetic = false;
        private String source = "IMPORT";
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

        public Builder isSynthetic(Boolean isSynthetic) {
            this.isSynthetic = isSynthetic;
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

        public SocialCandidate build() {
            return new SocialCandidate(id, platform, username, displayName, accountId, profileUrl,
                    description, avatarUrl, externalLinks, followerCount, isSynthetic, source, importedAt);
        }
    }
}
