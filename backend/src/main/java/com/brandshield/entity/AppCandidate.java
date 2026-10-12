package com.brandshield.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "app_candidates")
public class AppCandidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_name", nullable = false)
    private String appName;

    @Column(name = "package_id", length = 500)
    private String packageId;

    @Column(name = "bundle_id", length = 500)
    private String bundleId;

    @Column(name = "publisher_name")
    private String publisherName;

    @Column(name = "publisher_id")
    private String publisherId;

    @Column(name = "store_platform", length = 100)
    private String storePlatform;

    @Column(name = "store_url", length = 500)
    private String storeUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "icon_url", length = 500)
    private String iconUrl;

    @Column(name = "external_links", columnDefinition = "TEXT")
    private String externalLinks;

    @Column(name = "download_count", length = 100)
    private String downloadCount;

    @Column
    private Double rating;

    @Column(name = "is_synthetic")
    private Boolean isSynthetic = false;

    @Column(length = 100)
    private String source = "IMPORT";

    @Column(name = "imported_at")
    private LocalDateTime importedAt;

    public AppCandidate() {
    }

    public AppCandidate(Long id, String appName, String packageId, String bundleId, String publisherName,
                        String publisherId, String storePlatform, String storeUrl, String description,
                        String iconUrl, String externalLinks, String downloadCount, Double rating,
                        Boolean isSynthetic, String source, LocalDateTime importedAt) {
        this.id = id;
        this.appName = appName;
        this.packageId = packageId;
        this.bundleId = bundleId;
        this.publisherName = publisherName;
        this.publisherId = publisherId;
        this.storePlatform = storePlatform;
        this.storeUrl = storeUrl;
        this.description = description;
        this.iconUrl = iconUrl;
        this.externalLinks = externalLinks;
        this.downloadCount = downloadCount;
        this.rating = rating;
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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getStorePlatform() {
        return storePlatform;
    }

    public void setStorePlatform(String storePlatform) {
        this.storePlatform = storePlatform;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getExternalLinks() {
        return externalLinks;
    }

    public void setExternalLinks(String externalLinks) {
        this.externalLinks = externalLinks;
    }

    public String getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(String downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
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
        private String appName;
        private String packageId;
        private String bundleId;
        private String publisherName;
        private String publisherId;
        private String storePlatform;
        private String storeUrl;
        private String description;
        private String iconUrl;
        private String externalLinks;
        private String downloadCount;
        private Double rating;
        private Boolean isSynthetic = false;
        private String source = "IMPORT";
        private LocalDateTime importedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder appName(String appName) {
            this.appName = appName;
            return this;
        }

        public Builder packageId(String packageId) {
            this.packageId = packageId;
            return this;
        }

        public Builder bundleId(String bundleId) {
            this.bundleId = bundleId;
            return this;
        }

        public Builder publisherName(String publisherName) {
            this.publisherName = publisherName;
            return this;
        }

        public Builder publisherId(String publisherId) {
            this.publisherId = publisherId;
            return this;
        }

        public Builder storePlatform(String storePlatform) {
            this.storePlatform = storePlatform;
            return this;
        }

        public Builder storeUrl(String storeUrl) {
            this.storeUrl = storeUrl;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder iconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
            return this;
        }

        public Builder externalLinks(String externalLinks) {
            this.externalLinks = externalLinks;
            return this;
        }

        public Builder downloadCount(String downloadCount) {
            this.downloadCount = downloadCount;
            return this;
        }

        public Builder rating(Double rating) {
            this.rating = rating;
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

        public AppCandidate build() {
            return new AppCandidate(id, appName, packageId, bundleId, publisherName, publisherId,
                    storePlatform, storeUrl, description, iconUrl, externalLinks, downloadCount,
                    rating, isSynthetic, source, importedAt);
        }
    }
}
