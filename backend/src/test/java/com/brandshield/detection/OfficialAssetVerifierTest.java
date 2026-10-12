package com.brandshield.detection;

import com.brandshield.entity.AppCandidate;
import com.brandshield.entity.BrandAsset;
import com.brandshield.entity.SocialCandidate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OfficialAssetVerifierTest {

    private OfficialAssetVerifier verifier;

    @BeforeEach
    void setUp() {
        verifier = new OfficialAssetVerifier();
    }

    @Test
    @DisplayName("Social account matching registered official account ID must be verified as official")
    void testOfficialAccountMatchById() {
        BrandAsset officialAsset = BrandAsset.builder()
                .assetType(BrandAsset.AssetType.SOCIAL_ACCOUNT)
                .platform("Twitter")
                .identifier("official_123")
                .displayName("TechVault")
                .build();

        SocialCandidate candidate = SocialCandidate.builder()
                .platform("Twitter")
                .username("techvault")
                .accountId("official_123")
                .build();

        OfficialAssetVerifier.VerificationResult result =
                verifier.verifySocialCandidate(candidate, List.of(officialAsset));

        assertTrue(result.isOfficial());
        assertEquals("ACCOUNT_ID_MATCH", result.matchType());
    }

    @Test
    @DisplayName("Social account matching registered canonical URL must be verified as official")
    void testOfficialAccountMatchByUrl() {
        BrandAsset officialAsset = BrandAsset.builder()
                .assetType(BrandAsset.AssetType.SOCIAL_ACCOUNT)
                .platform("Twitter")
                .identifier("twitter_tv")
                .canonicalUrl("https://twitter.com/techvault")
                .build();

        SocialCandidate candidate = SocialCandidate.builder()
                .platform("Twitter")
                .username("techvault")
                .profileUrl("https://www.twitter.com/techvault/")
                .build();

        OfficialAssetVerifier.VerificationResult result =
                verifier.verifySocialCandidate(candidate, List.of(officialAsset));

        assertTrue(result.isOfficial());
        assertEquals("CANONICAL_URL_MATCH", result.matchType());
    }

    @Test
    @DisplayName("Social candidate with different ID must not be verified even if username is similar")
    void testNonOfficialSocialCandidate() {
        BrandAsset officialAsset = BrandAsset.builder()
                .assetType(BrandAsset.AssetType.SOCIAL_ACCOUNT)
                .platform("Twitter")
                .identifier("official_123")
                .build();

        SocialCandidate candidate = SocialCandidate.builder()
                .platform("Twitter")
                .username("techvault")
                .accountId("imposter_999")
                .build();

        OfficialAssetVerifier.VerificationResult result =
                verifier.verifySocialCandidate(candidate, List.of(officialAsset));

        assertFalse(result.isOfficial());
    }

    @Test
    @DisplayName("Mobile app matching registered official package ID must be verified as official")
    void testOfficialAppMatchByPackageId() {
        BrandAsset officialApp = BrandAsset.builder()
                .assetType(BrandAsset.AssetType.MOBILE_APP)
                .platform("Google Play")
                .identifier("com.techvault.app")
                .displayName("TechVault Inc")
                .build();

        AppCandidate candidate = AppCandidate.builder()
                .appName("TechVault - Cloud Storage")
                .packageId("com.techvault.app")
                .publisherName("TechVault Inc")
                .build();

        OfficialAssetVerifier.VerificationResult result =
                verifier.verifyAppCandidate(candidate, List.of(officialApp));

        assertTrue(result.isOfficial());
        assertEquals("PACKAGE_ID_MATCH", result.matchType());
    }

    @Test
    @DisplayName("Mobile app with look-alike package ID must NOT be verified as official")
    void testLookAlikeAppPackageIdNotOfficial() {
        BrandAsset officialApp = BrandAsset.builder()
                .assetType(BrandAsset.AssetType.MOBILE_APP)
                .platform("Google Play")
                .identifier("com.techvault.app")
                .build();

        AppCandidate candidate = AppCandidate.builder()
                .appName("TechVau1t App")
                .packageId("com.techvau1t.app")
                .publisherName("Unknown Publisher")
                .build();

        OfficialAssetVerifier.VerificationResult result =
                verifier.verifyAppCandidate(candidate, List.of(officialApp));

        assertFalse(result.isOfficial(), "Look-alike package ID must never match official whitelist");
    }
}
