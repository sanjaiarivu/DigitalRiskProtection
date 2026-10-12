package com.brandshield.service;

import com.brandshield.dto.request.BrandAssetCreateRequest;
import com.brandshield.dto.request.BrandCreateRequest;
import com.brandshield.dto.response.BrandAssetResponse;
import com.brandshield.dto.response.BrandDetailResponse;
import com.brandshield.dto.response.BrandResponse;
import com.brandshield.entity.Brand;
import com.brandshield.entity.BrandAlias;
import com.brandshield.entity.BrandAsset;
import com.brandshield.exception.ResourceNotFoundException;
import com.brandshield.mapper.EntityMapper;
import com.brandshield.repository.BrandAssetRepository;
import com.brandshield.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandAssetRepository brandAssetRepository;

    public BrandService(BrandRepository brandRepository, BrandAssetRepository brandAssetRepository) {
        this.brandRepository = brandRepository;
        this.brandAssetRepository = brandAssetRepository;
    }

    @Transactional
    public BrandResponse createBrand(BrandCreateRequest request) {
        Brand brand = Brand.builder()
                .name(request.getName())
                .description(request.getDescription())
                .website(request.getWebsite())
                .logoUrl(request.getLogoUrl())
                .build();

        if (request.getAliases() != null) {
            for (String aliasName : request.getAliases()) {
                if (aliasName != null && !aliasName.isBlank()) {
                    BrandAlias alias = BrandAlias.builder().aliasName(aliasName.trim()).build();
                    brand.addAlias(alias);
                }
            }
        }

        brand = brandRepository.save(brand);
        return EntityMapper.toBrandResponse(brand);
    }

    @Transactional(readOnly = true)
    public BrandDetailResponse getBrand(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", id));
        return EntityMapper.toBrandDetailResponse(brand);
    }

    @Transactional(readOnly = true)
    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(EntityMapper::toBrandResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BrandResponse updateBrand(Long id, BrandCreateRequest request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", id));

        brand.setName(request.getName());
        brand.setDescription(request.getDescription());
        brand.setWebsite(request.getWebsite());
        brand.setLogoUrl(request.getLogoUrl());

        // Update aliases
        brand.getAliases().clear();
        if (request.getAliases() != null) {
            for (String aliasName : request.getAliases()) {
                if (aliasName != null && !aliasName.isBlank()) {
                    BrandAlias alias = BrandAlias.builder().aliasName(aliasName.trim()).build();
                    brand.addAlias(alias);
                }
            }
        }

        brand = brandRepository.save(brand);
        return EntityMapper.toBrandResponse(brand);
    }

    @Transactional
    public void deleteBrand(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new ResourceNotFoundException("Brand", id);
        }
        brandRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<BrandAssetResponse> getAssets(Long brandId) {
        if (!brandRepository.existsById(brandId)) {
            throw new ResourceNotFoundException("Brand", brandId);
        }
        return brandAssetRepository.findByBrandId(brandId).stream()
                .map(EntityMapper::toBrandAssetResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BrandAssetResponse addAsset(Long brandId, BrandAssetCreateRequest request) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", brandId));

        BrandAsset asset = BrandAsset.builder()
                .assetType(BrandAsset.AssetType.valueOf(request.getAssetType()))
                .platform(request.getPlatform())
                .identifier(request.getIdentifier())
                .displayName(request.getDisplayName())
                .canonicalUrl(request.getCanonicalUrl())
                .verified(true)
                .build();

        brand.addAsset(asset);
        brandRepository.save(brand);

        return EntityMapper.toBrandAssetResponse(asset);
    }

    @Transactional
    public void deleteAsset(Long brandId, Long assetId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", brandId));

        brand.getAssets().removeIf(a -> a.getId().equals(assetId));
        brandRepository.save(brand);
    }
}
