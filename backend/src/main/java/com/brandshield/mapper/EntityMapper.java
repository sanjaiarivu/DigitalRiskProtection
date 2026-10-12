package com.brandshield.mapper;

import com.brandshield.dto.response.*;
import com.brandshield.entity.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manual entity-to-DTO mapper. Avoids MapStruct dependency while keeping
 * mapping logic centralized and testable.
 */
public final class EntityMapper {

    private EntityMapper() {}

    public static BrandResponse toBrandResponse(Brand brand) {
        return BrandResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .website(brand.getWebsite())
                .logoUrl(brand.getLogoUrl())
                .aliases(brand.getAliases() != null
                    ? brand.getAliases().stream().map(BrandAlias::getAliasName).collect(Collectors.toList())
                    : List.of())
                .assetCount(brand.getAssets() != null ? brand.getAssets().size() : 0)
                .createdAt(brand.getCreatedAt())
                .build();
    }

    public static BrandDetailResponse toBrandDetailResponse(Brand brand) {
        return BrandDetailResponse.builder()
                .id(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .website(brand.getWebsite())
                .logoUrl(brand.getLogoUrl())
                .aliases(brand.getAliases() != null
                    ? brand.getAliases().stream().map(BrandAlias::getAliasName).collect(Collectors.toList())
                    : List.of())
                .assets(brand.getAssets() != null
                    ? brand.getAssets().stream().map(EntityMapper::toBrandAssetResponse).collect(Collectors.toList())
                    : List.of())
                .createdAt(brand.getCreatedAt())
                .updatedAt(brand.getUpdatedAt())
                .build();
    }

    public static BrandAssetResponse toBrandAssetResponse(BrandAsset asset) {
        return BrandAssetResponse.builder()
                .id(asset.getId())
                .assetType(asset.getAssetType().name())
                .platform(asset.getPlatform())
                .identifier(asset.getIdentifier())
                .displayName(asset.getDisplayName())
                .canonicalUrl(asset.getCanonicalUrl())
                .verified(asset.getVerified() != null && asset.getVerified())
                .createdAt(asset.getCreatedAt())
                .build();
    }

    public static SocialCandidateResponse toSocialCandidateResponse(SocialCandidate c) {
        return SocialCandidateResponse.builder()
                .id(c.getId())
                .platform(c.getPlatform())
                .username(c.getUsername())
                .displayName(c.getDisplayName())
                .accountId(c.getAccountId())
                .profileUrl(c.getProfileUrl())
                .description(c.getDescription())
                .avatarUrl(c.getAvatarUrl())
                .externalLinks(c.getExternalLinks())
                .followerCount(c.getFollowerCount())
                .synthetic(c.getIsSynthetic() != null && c.getIsSynthetic())
                .source(c.getSource())
                .importedAt(c.getImportedAt())
                .build();
    }

    public static AppCandidateResponse toAppCandidateResponse(AppCandidate c) {
        return AppCandidateResponse.builder()
                .id(c.getId())
                .appName(c.getAppName())
                .packageId(c.getPackageId())
                .bundleId(c.getBundleId())
                .publisherName(c.getPublisherName())
                .publisherId(c.getPublisherId())
                .storePlatform(c.getStorePlatform())
                .storeUrl(c.getStoreUrl())
                .description(c.getDescription())
                .iconUrl(c.getIconUrl())
                .externalLinks(c.getExternalLinks())
                .downloadCount(c.getDownloadCount())
                .rating(c.getRating())
                .synthetic(c.getIsSynthetic() != null && c.getIsSynthetic())
                .source(c.getSource())
                .importedAt(c.getImportedAt())
                .build();
    }

    public static ThreatDetectionResponse toThreatDetectionResponse(ThreatDetection d) {
        String candidateName = null;
        String candidatePlatform = null;
        if (d.getCandidateType() == ThreatDetection.CandidateType.SOCIAL && d.getSocialCandidate() != null) {
            candidateName = d.getSocialCandidate().getDisplayName() != null
                ? d.getSocialCandidate().getDisplayName()
                : d.getSocialCandidate().getUsername();
            candidatePlatform = d.getSocialCandidate().getPlatform();
        } else if (d.getCandidateType() == ThreatDetection.CandidateType.APP && d.getAppCandidate() != null) {
            candidateName = d.getAppCandidate().getAppName();
            candidatePlatform = d.getAppCandidate().getStorePlatform();
        }
        return ThreatDetectionResponse.builder()
                .id(d.getId())
                .brandId(d.getBrand().getId())
                .brandName(d.getBrand().getName())
                .detectionType(d.getDetectionType().name())
                .candidateType(d.getCandidateType().name())
                .riskScore(d.getRiskScore())
                .riskLevel(d.getRiskLevel().name())
                .status(d.getStatus().name())
                .officialAsset(d.getIsOfficialAsset() != null && d.getIsOfficialAsset())
                .summary(d.getSummary())
                .candidateName(candidateName)
                .candidatePlatform(candidatePlatform)
                .detectedAt(d.getDetectedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }

    public static ThreatDetectionDetailResponse toThreatDetectionDetailResponse(ThreatDetection d) {
        String candidateName = null;
        String candidatePlatform = null;
        SocialCandidateResponse socialResp = null;
        AppCandidateResponse appResp = null;

        if (d.getCandidateType() == ThreatDetection.CandidateType.SOCIAL && d.getSocialCandidate() != null) {
            candidateName = d.getSocialCandidate().getDisplayName() != null
                ? d.getSocialCandidate().getDisplayName()
                : d.getSocialCandidate().getUsername();
            candidatePlatform = d.getSocialCandidate().getPlatform();
            socialResp = toSocialCandidateResponse(d.getSocialCandidate());
        } else if (d.getCandidateType() == ThreatDetection.CandidateType.APP && d.getAppCandidate() != null) {
            candidateName = d.getAppCandidate().getAppName();
            candidatePlatform = d.getAppCandidate().getStorePlatform();
            appResp = toAppCandidateResponse(d.getAppCandidate());
        }

        return ThreatDetectionDetailResponse.builder()
                .id(d.getId())
                .brandId(d.getBrand().getId())
                .brandName(d.getBrand().getName())
                .detectionType(d.getDetectionType().name())
                .candidateType(d.getCandidateType().name())
                .riskScore(d.getRiskScore())
                .riskLevel(d.getRiskLevel().name())
                .status(d.getStatus().name())
                .officialAsset(d.getIsOfficialAsset() != null && d.getIsOfficialAsset())
                .summary(d.getSummary())
                .aiExplanation(d.getAiExplanation())
                .candidateName(candidateName)
                .candidatePlatform(candidatePlatform)
                .detectedAt(d.getDetectedAt())
                .updatedAt(d.getUpdatedAt())
                .evidence(d.getEvidence() != null
                    ? d.getEvidence().stream().map(EntityMapper::toEvidenceResponse).collect(Collectors.toList())
                    : List.of())
                .notes(d.getNotes() != null
                    ? d.getNotes().stream().map(EntityMapper::toNoteResponse).collect(Collectors.toList())
                    : List.of())
                .history(d.getHistory() != null
                    ? d.getHistory().stream().map(EntityMapper::toHistoryResponse).collect(Collectors.toList())
                    : List.of())
                .socialCandidate(socialResp)
                .appCandidate(appResp)
                .build();
    }

    public static DetectionEvidenceResponse toEvidenceResponse(DetectionEvidence e) {
        return DetectionEvidenceResponse.builder()
                .id(e.getId())
                .signalName(e.getSignalName())
                .signalCategory(e.getSignalCategory())
                .observedValue(e.getObservedValue())
                .expectedValue(e.getExpectedValue())
                .evidenceExplanation(e.getEvidenceExplanation())
                .scoreContribution(e.getScoreContribution())
                .confidence(e.getConfidence())
                .build();
    }

    public static InvestigationNoteResponse toNoteResponse(InvestigationNote n) {
        return InvestigationNoteResponse.builder()
                .id(n.getId())
                .author(n.getAuthor())
                .noteText(n.getNoteText())
                .createdAt(n.getCreatedAt())
                .build();
    }

    public static InvestigationHistoryResponse toHistoryResponse(InvestigationHistory h) {
        return InvestigationHistoryResponse.builder()
                .id(h.getId())
                .previousStatus(h.getPreviousStatus())
                .newStatus(h.getNewStatus())
                .changedBy(h.getChangedBy())
                .changeReason(h.getChangeReason())
                .changedAt(h.getChangedAt())
                .build();
    }
}
