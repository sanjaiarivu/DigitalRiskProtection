package com.brandshield.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class BrandCreateRequest {

    @NotBlank(message = "Brand name is required")
    @Size(max = 255, message = "Brand name must be at most 255 characters")
    private String name;

    @Size(max = 2000, message = "Description must be at most 2000 characters")
    private String description;

    @Size(max = 500, message = "Website URL must be at most 500 characters")
    private String website;

    @Size(max = 500, message = "Logo URL must be at most 500 characters")
    private String logoUrl;

    private List<String> aliases;

    public BrandCreateRequest() {
    }

    public BrandCreateRequest(String name, String description, String website, String logoUrl, List<String> aliases) {
        this.name = name;
        this.description = description;
        this.website = website;
        this.logoUrl = logoUrl;
        this.aliases = aliases;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;
        private String website;
        private String logoUrl;
        private List<String> aliases;

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

        public BrandCreateRequest build() {
            return new BrandCreateRequest(name, description, website, logoUrl, aliases);
        }
    }
}
