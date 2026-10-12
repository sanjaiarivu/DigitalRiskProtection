package com.brandshield.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class BrandDetailResponse {
    private Long id;
    private String name;
    private String description;
    private String website;
    private String logoUrl;
    private List<String> aliases;
    private List<BrandAssetResponse> assets;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BrandDetailResponse() {
    }

    public BrandDetailResponse(Long id, String name, String description, String website, String logoUrl,
                               List<String> aliases, List<BrandAssetResponse> assets,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.website = website;
        this.logoUrl = logoUrl;
        this.aliases = aliases;
        this.assets = assets;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public List<BrandAssetResponse> getAssets() {
        return assets;
    }

    public void setAssets(List<BrandAssetResponse> assets) {
        this.assets = assets;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String description;
        private String website;
        private String logoUrl;
        private List<String> aliases;
        private List<BrandAssetResponse> assets;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder website(String website) {
            this.website = website;
            return this;
        }

        public Builder logoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
            return this;
        }

        public Builder aliases(List<String> aliases) {
            this.aliases = aliases;
            return this;
        }

        public Builder assets(List<BrandAssetResponse> assets) {
            this.assets = assets;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public BrandDetailResponse build() {
            return new BrandDetailResponse(id, name, description, website, logoUrl, aliases, assets, createdAt, updatedAt);
        }
    }
}
