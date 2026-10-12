package com.brandshield.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "brand_assets")
public class BrandAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "asset_type", nullable = false, length = 50)
    private AssetType assetType;

    @Column(length = 100)
    private String platform;

    @Column(nullable = false, length = 500)
    private String identifier;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "canonical_url", length = 500)
    private String canonicalUrl;

    @Column
    private Boolean verified = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public BrandAsset() {
    }

    public BrandAsset(Long id, Brand brand, AssetType assetType, String platform, String identifier,
                      String displayName, String canonicalUrl, Boolean verified, LocalDateTime createdAt) {
        this.id = id;
        this.brand = brand;
        this.assetType = assetType;
        this.platform = platform;
        this.identifier = identifier;
        this.displayName = displayName;
        this.canonicalUrl = canonicalUrl;
        this.verified = verified != null ? verified : false;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum AssetType {
        SOCIAL_ACCOUNT, MOBILE_APP, DOMAIN, LOGO
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
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

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
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
        private Brand brand;
        private AssetType assetType;
        private String platform;
        private String identifier;
        private String displayName;
        private String canonicalUrl;
        private Boolean verified = false;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder brand(Brand brand) {
            this.brand = brand;
            return this;
        }

        public Builder assetType(AssetType assetType) {
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

        public Builder verified(Boolean verified) {
            this.verified = verified;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public BrandAsset build() {
            return new BrandAsset(id, brand, assetType, platform, identifier, displayName, canonicalUrl, verified, createdAt);
        }
    }
}
