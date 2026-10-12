package com.brandshield.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BrandAssetCreateRequest {

    @NotNull(message = "Asset type is required")
    private String assetType;

    private String platform;

    @NotBlank(message = "Identifier is required")
    @Size(max = 500, message = "Identifier must be at most 500 characters")
    private String identifier;

    @Size(max = 255, message = "Display name must be at most 255 characters")
    private String displayName;

    @Size(max = 500, message = "Canonical URL must be at most 500 characters")
    private String canonicalUrl;

    public BrandAssetCreateRequest() {
    }

    public BrandAssetCreateRequest(String assetType, String platform, String identifier, String displayName, String canonicalUrl) {
        this.assetType = assetType;
        this.platform = platform;
        this.identifier = identifier;
        this.displayName = displayName;
        this.canonicalUrl = canonicalUrl;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String assetType;
        private String platform;
        private String identifier;
        private String displayName;
        private String canonicalUrl;

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

        public BrandAssetCreateRequest build() {
            return new BrandAssetCreateRequest(assetType, platform, identifier, displayName, canonicalUrl);
        }
    }
}
