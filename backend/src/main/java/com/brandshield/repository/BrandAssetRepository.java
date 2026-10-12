package com.brandshield.repository;

import com.brandshield.entity.BrandAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BrandAssetRepository extends JpaRepository<BrandAsset, Long> {
    List<BrandAsset> findByBrandId(Long brandId);
    Optional<BrandAsset> findByAssetTypeAndIdentifier(BrandAsset.AssetType assetType, String identifier);
    List<BrandAsset> findByAssetTypeAndPlatformIgnoreCase(BrandAsset.AssetType assetType, String platform);
    List<BrandAsset> findByBrandIdAndAssetType(Long brandId, BrandAsset.AssetType assetType);
}
