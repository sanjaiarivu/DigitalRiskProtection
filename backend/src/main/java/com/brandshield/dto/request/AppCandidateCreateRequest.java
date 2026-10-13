package com.brandshield.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AppCandidateCreateRequest(
        @NotBlank @Size(max = 255) String appName,
        @Size(max = 500) String packageId,
        @Size(max = 500) String bundleId,
        @Size(max = 255) String publisherName,
        @Size(max = 255) String publisherId,
        @Size(max = 100) String storePlatform,
        @Size(max = 500) String storeUrl,
        String description,
        @Size(max = 500) String iconUrl,
        String externalLinks,
        @Size(max = 100) String downloadCount,
        Double rating) {}
