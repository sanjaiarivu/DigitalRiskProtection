package com.brandshield.dto.response;

import java.time.LocalDateTime;

public class AppCandidateResponse {
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
    private boolean synthetic;
    private String source;
    private LocalDateTime importedAt;

    public AppCandidateResponse() {
    }

    public AppCandidateResponse(Long id, String appName, String packageId, String bundleId,
                                String publisherName, String publisherId, String storePlatform,
                                String storeUrl, String description, String iconUrl,
                                String externalLinks, String downloadCount, Double rating,
                                boolean synthetic, String source, LocalDateTime importedAt) {
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
        private boolean synthetic;
        private String source;
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

        public AppCandidateResponse build() {
            return new AppCandidateResponse(id, appName, packageId, bundleId, publisherName,
                    publisherId, storePlatform, storeUrl, description, iconUrl,
                    externalLinks, downloadCount, rating, synthetic, source, importedAt);
        }
    }
}
