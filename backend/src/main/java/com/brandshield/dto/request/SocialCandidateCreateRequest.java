package com.brandshield.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SocialCandidateCreateRequest(
        @NotBlank @Size(max = 100) String platform,
        @Size(max = 255) String username,
        @Size(max = 255) String displayName,
        @Size(max = 255) String accountId,
        @Size(max = 500) String profileUrl,
        String description,
        @Size(max = 500) String avatarUrl,
        String externalLinks,
        Integer followerCount) {}
