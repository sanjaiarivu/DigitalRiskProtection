package com.brandshield.dto.response;

import java.time.LocalDateTime;

public class BrandAssetResponse {
    private Long id;
    private String assetType;
    private String platform;
    private String identifier;
    private String displayName;
    private String canonicalUrl;
    private boolean verified;
    private LocalDateTime createdAt;

    public BrandAssetResponse() {
    }

    public BrandAssetResponse(Long id, String assetType, String platform, String identifier,
                              String displayName, String canonicalUrl, boolean verified,
                              LocalDateTime createdAt) {
        this.id = id;
        this.assetType = assetType;
        this.platform = platform;
        this.identifier = identifier;
        this.displayName = displayName;
        this.canonicalUrl = canonicalUrl;
        this.verified = verified;
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

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public void setCanonicalUrl(String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    public boolean isVerified() {
        return verified;
    }

    public boolean getVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static class Builder {
        private Long id;
        private String assetType;
        private String platform;
        private String identifier;
        private String displayName;
        private String canonicalUrl;
        private boolean verified;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder assetType(String assetType) {
            this.assetType = assetType;
            return this;
        }

        public Builder platform(String platform) {
            this.platform = platform;
            return this;
        }

        public Builder identifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder canonicalUrl(String canonicalUrl) {
            this.canonicalUrl = canonicalUrl;
            return this;
        }

        public Builder verified(boolean verified) {
            this.verified = verified;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public BrandAssetResponse build() {
            return new BrandAssetResponse(id, assetType, platform, identifier, displayName, canonicalUrl, verified, createdAt);
        }
    }
}
