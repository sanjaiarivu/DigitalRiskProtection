package com.brandshield.detection;

import com.brandshield.entity.AppCandidate;
import com.brandshield.entity.BrandAsset;
import com.brandshield.entity.SocialCandidate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Verifies whether a candidate (social profile or app) matches a registered
 * official brand asset.
 *
 * Matching is based on exact identifier comparison (account IDs, package IDs,
 * publisher IDs, canonical URLs), NOT on display name similarity. This prevents
 * official assets from being falsely flagged as impersonators.
 *
 * Design rationale: Display names can be changed freely on most platforms,
 * so they are unreliable identifiers. Platform-assigned IDs and package
 * identifiers are stable and authoritative.
 */
@Component
public class OfficialAssetVerifier {

    /**
     * Checks if a social candidate matches any registered official social account.
     */
    public VerificationResult verifySocialCandidate(SocialCandidate candidate, List<BrandAsset> officialAssets) {
        if (candidate == null || officialAssets == null || officialAssets.isEmpty()) {
            return VerificationResult.notOfficial();
        }

        for (BrandAsset asset : officialAssets) {
            if (asset.getAssetType() != BrandAsset.AssetType.SOCIAL_ACCOUNT) continue;

            // Match 1: Exact account ID match (strongest signal)
            if (candidate.getAccountId() != null && !candidate.getAccountId().isBlank()
                && candidate.getAccountId().equalsIgnoreCase(asset.getIdentifier())) {
                return VerificationResult.official(asset, "ACCOUNT_ID_MATCH");
            }

            // Match 2: Canonical URL match
            if (candidate.getProfileUrl() != null && !candidate.getProfileUrl().isBlank()
                && asset.getCanonicalUrl() != null && !asset.getCanonicalUrl().isBlank()
                && normalizeUrl(candidate.getProfileUrl()).equalsIgnoreCase(normalizeUrl(asset.getCanonicalUrl()))) {
                return VerificationResult.official(asset, "CANONICAL_URL_MATCH");
            }

            // Match 3: Platform + identifier (username) match
            if (candidate.getPlatform() != null && asset.getPlatform() != null
                && candidate.getPlatform().equalsIgnoreCase(asset.getPlatform())) {
                if (candidate.getUsername() != null && !candidate.getUsername().isBlank()
                    && candidate.getUsername().equalsIgnoreCase(asset.getIdentifier())) {
                    return VerificationResult.official(asset, "PLATFORM_USERNAME_MATCH");
                }
            }
        }

        return VerificationResult.notOfficial();
    }

    /**
     * Checks if an app candidate matches any registered official mobile app.
     */
    public VerificationResult verifyAppCandidate(AppCandidate candidate, List<BrandAsset> officialAssets) {
        if (candidate == null || officialAssets == null || officialAssets.isEmpty()) {
            return VerificationResult.notOfficial();
        }

        for (BrandAsset asset : officialAssets) {
            if (asset.getAssetType() != BrandAsset.AssetType.MOBILE_APP) continue;

            // Match 1: Exact package ID match
            if (candidate.getPackageId() != null && !candidate.getPackageId().isBlank()
                && candidate.getPackageId().equalsIgnoreCase(asset.getIdentifier())) {
                return VerificationResult.official(asset, "PACKAGE_ID_MATCH");
            }

            // Match 2: Bundle ID match
            if (candidate.getBundleId() != null && !candidate.getBundleId().isBlank()
                && candidate.getBundleId().equalsIgnoreCase(asset.getIdentifier())) {
                return VerificationResult.official(asset, "BUNDLE_ID_MATCH");
            }
        }

        return VerificationResult.notOfficial();
    }

    /**
     * Normalizes URL for comparison: removes protocol, www prefix, and trailing slash.
     */
    private String normalizeUrl(String url) {
        if (url == null) return "";
        String normalized = url.trim().toLowerCase();
        normalized = normalized.replaceFirst("^https?://", "");
        normalized = normalized.replaceFirst("^www\\.", "");
        if (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    public record VerificationResult(
        boolean isOfficial,
        BrandAsset matchedAsset,
        String matchType
    ) {
        public static VerificationResult official(BrandAsset asset, String matchType) {
            return new VerificationResult(true, asset, matchType);
        }

        public static VerificationResult notOfficial() {
            return new VerificationResult(false, null, null);
        }
    }
}
