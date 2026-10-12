package com.brandshield.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "brand_aliases")
public class BrandAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Column(name = "alias_name", nullable = false)
    private String aliasName;

    public BrandAlias() {
    }

    public BrandAlias(Long id, Brand brand, String aliasName) {
        this.id = id;
        this.brand = brand;
        this.aliasName = aliasName;
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

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Brand brand;
        private String aliasName;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder brand(Brand brand) {
            this.brand = brand;
            return this;
        }

        public Builder aliasName(String aliasName) {
            this.aliasName = aliasName;
            return this;
        }

        public BrandAlias build() {
            return new BrandAlias(id, brand, aliasName);
        }
    }
}
