package com.brandshield.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "brands")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String website;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BrandAlias> aliases = new ArrayList<>();

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BrandAsset> assets = new ArrayList<>();

    public Brand() {
    }

    public Brand(Long id, String name, String description, String website, String logoUrl,
                 LocalDateTime createdAt, LocalDateTime updatedAt, List<BrandAlias> aliases, List<BrandAsset> assets) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.website = website;
        this.logoUrl = logoUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.aliases = aliases != null ? aliases : new ArrayList<>();
        this.assets = assets != null ? assets : new ArrayList<>();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addAlias(BrandAlias alias) {
        aliases.add(alias);
        alias.setBrand(this);
    }

    public void addAsset(BrandAsset asset) {
        assets.add(asset);
        asset.setBrand(this);
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

    public List<BrandAlias> getAliases() {
        return aliases;
    }

    public void setAliases(List<BrandAlias> aliases) {
        this.aliases = aliases;
    }

    public List<BrandAsset> getAssets() {
        return assets;
    }

    public void setAssets(List<BrandAsset> assets) {
        this.assets = assets;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String description;
        private String website;
        private String logoUrl;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<BrandAlias> aliases = new ArrayList<>();
        private List<BrandAsset> assets = new ArrayList<>();

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

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder aliases(List<BrandAlias> aliases) {
            this.aliases = aliases;
            return this;
        }

        public Builder assets(List<BrandAsset> assets) {
            this.assets = assets;
            return this;
        }

        public Brand build() {
            return new Brand(id, name, description, website, logoUrl, createdAt, updatedAt, aliases, assets);
        }
    }
}
