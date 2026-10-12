package com.brandshield.controller;

import com.brandshield.dto.request.BrandAssetCreateRequest;
import com.brandshield.dto.request.BrandCreateRequest;
import com.brandshield.dto.response.BrandAssetResponse;
import com.brandshield.dto.response.BrandDetailResponse;
import com.brandshield.dto.response.BrandResponse;
import com.brandshield.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@CrossOrigin(origins = "*")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity<BrandResponse> createBrand(@Valid @RequestBody BrandCreateRequest request) {
        BrandResponse response = brandService.createBrand(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAllBrands() {
        return ResponseEntity.ok(brandService.getAllBrands());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDetailResponse> getBrand(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.getBrand(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandResponse> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandCreateRequest request) {
        return ResponseEntity.ok(brandService.updateBrand(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/assets")
    public ResponseEntity<List<BrandAssetResponse>> getAssets(@PathVariable Long id) {
        return ResponseEntity.ok(brandService.getAssets(id));
    }

    @PostMapping("/{id}/assets")
    public ResponseEntity<BrandAssetResponse> addAsset(
            @PathVariable Long id,
            @Valid @RequestBody BrandAssetCreateRequest request) {
        BrandAssetResponse response = brandService.addAsset(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{brandId}/assets/{assetId}")
    public ResponseEntity<Void> deleteAsset(
            @PathVariable Long brandId,
            @PathVariable Long assetId) {
        brandService.deleteAsset(brandId, assetId);
        return ResponseEntity.noContent().build();
    }
}
