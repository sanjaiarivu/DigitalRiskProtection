package com.brandshield.detection;

import com.brandshield.entity.AppCandidate;
import com.brandshield.entity.Brand;
import com.brandshield.entity.BrandAlias;
import com.brandshield.entity.BrandAsset;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Analyzes mobile app candidates for brand impersonation signals.
 * Checks app name similarity, publisher mismatches, package ID conflicts,
 * and suspicious metadata.
 */
@Component
public class AppCandidateAnalyzer {

    private final TextSimilarityAnalyzer textAnalyzer;
    private static final double SIMILARITY_THRESHOLD = 0.5;

    public AppCandidateAnalyzer(TextSimilarityAnalyzer textAnalyzer) {
        this.textAnalyzer = textAnalyzer;
    }

    /**
     * Analyzes an app candidate against a brand and its official assets.
     */
    public List<DetectionSignal> analyze(AppCandidate candidate, Brand brand, List<BrandAsset> officialApps) {
        List<DetectionSignal> signals = new ArrayList<>();

        List<String> brandNames = new ArrayList<>();
        brandNames.add(brand.getName());
        if (brand.getAliases() != null) {
            brand.getAliases().stream()
                .map(BrandAlias::getAliasName)
                .forEach(brandNames::add);
        }

        // App name similarity
        for (String brandName : brandNames) {
            analyzeAppNameSignals(signals, brandName, candidate.getAppName());
        }

        // Publisher mismatch detection
        if (candidate.getPublisherName() != null && !candidate.getPublisherName().isBlank()) {
            analyzePublisherMismatch(signals, candidate, brand, officialApps);
        }

        // Package ID analysis
        if (candidate.getPackageId() != null && !candidate.getPackageId().isBlank()) {
            analyzePackageId(signals, candidate.getPackageId(), brand, officialApps);
        }

        // Description analysis
        if (candidate.getDescription() != null && !candidate.getDescription().isBlank()) {
            analyzeAppDescription(signals, candidate.getDescription(), brand);
        }

        // External links
        if (candidate.getExternalLinks() != null && !candidate.getExternalLinks().isBlank()) {
            analyzeAppLinks(signals, candidate.getExternalLinks(), brand);
        }

        return signals;
    }

    private void analyzeAppNameSignals(List<DetectionSignal> signals, String brandName, String appName) {
        TextSimilarityAnalyzer.NameSimilarityResult result =
            textAnalyzer.analyzeNameSimilarity(brandName, appName);

        if (result.overallSimilarityScore() < SIMILARITY_THRESHOLD) return;

        if (result.normalizedSimilarity() >= 0.6) {
            int contribution = (int) (result.normalizedSimilarity() * 25);
            signals.add(new DetectionSignal(
                "APP_NAME_SIMILARITY",
                DetectionSignal.Category.NAME_SIMILARITY.name(),
                appName,
                brandName,
                String.format("App name '%s' has %.0f%% similarity to brand '%s'",
                    appName, result.normalizedSimilarity() * 100, brandName),
                contribution,
                result.normalizedSimilarity() >= 0.85
                    ? DetectionSignal.Confidence.HIGH
                    : DetectionSignal.Confidence.MEDIUM
            ));
        }

        if (result.hasCharSubstitutions()) {
            signals.add(new DetectionSignal(
                "APP_NAME_CHAR_SUBSTITUTION",
                DetectionSignal.Category.CHARACTER_SUBSTITUTION.name(),
                appName,
                brandName,
                String.format("App name contains look-alike character substitutions: %s",
                    String.join("; ", result.substitutionDetails().values())),
                20,
                DetectionSignal.Confidence.HIGH
            ));
        }

        if (textAnalyzer.hasAdjacentTransposition(brandName, appName)) {
            signals.add(new DetectionSignal(
                "APP_NAME_CHARACTER_TRANSPOSITION",
                DetectionSignal.Category.CHARACTER_SUBSTITUTION.name(),
                appName,
                brandName,
                String.format("Adjacent characters in app name '%s' are swapped relative to brand '%s'", appName, brandName),
                18,
                DetectionSignal.Confidence.MEDIUM
            ));
        }
    }

    /**
     * Detects publisher mismatches: the app name looks like the brand,
     * but the publisher doesn't match any registered official publisher.
     */
    private void analyzePublisherMismatch(List<DetectionSignal> signals,
                                           AppCandidate candidate, Brand brand,
                                           List<BrandAsset> officialApps) {
        List<String> officialPublishers = officialApps.stream()
            .filter(a -> a.getAssetType() == BrandAsset.AssetType.MOBILE_APP)
            .map(BrandAsset::getDisplayName)
            .filter(name -> name != null && !name.isBlank())
            .toList();

        if (!officialPublishers.isEmpty()) {
            boolean publisherMatch = officialPublishers.stream()
                .anyMatch(pub -> pub.equalsIgnoreCase(candidate.getPublisherName()));

            if (!publisherMatch) {
                double nameSim = textAnalyzer.calculateNormalizedSimilarity(
                    brand.getName(), candidate.getAppName());
                if (nameSim >= 0.6) {
                    signals.add(new DetectionSignal(
                        "PUBLISHER_MISMATCH",
                        DetectionSignal.Category.PUBLISHER_MISMATCH.name(),
                        candidate.getPublisherName(),
                        "Registered official publisher",
                        String.format("App publisher '%s' does not match registered official publisher for '%s'",
                            candidate.getPublisherName(), brand.getName()),
                        25,
                        DetectionSignal.Confidence.HIGH
                    ));
                }
            }
        }
    }

    private void analyzePackageId(List<DetectionSignal> signals, String packageId,
                                    Brand brand, List<BrandAsset> officialApps) {
        for (BrandAsset officialApp : officialApps) {
            if (officialApp.getAssetType() != BrandAsset.AssetType.MOBILE_APP) continue;
            String officialPkg = officialApp.getIdentifier();
            if (officialPkg == null) continue;

            double similarity = textAnalyzer.calculateNormalizedSimilarity(packageId, officialPkg);
            if (similarity >= 0.7 && similarity < 1.0) {
                signals.add(new DetectionSignal(
                    "PACKAGE_ID_SIMILARITY",
                    DetectionSignal.Category.IDENTIFIER_CONFLICT.name(),
                    packageId,
                    officialPkg,
                    String.format("Package ID '%s' is %.0f%% similar to official '%s' but not an exact match",
                        packageId, similarity * 100, officialPkg),
                    30,
                    DetectionSignal.Confidence.HIGH
                ));
            }
        }
    }

    private void analyzeAppDescription(List<DetectionSignal> signals, String description, Brand brand) {
        String normalizedDesc = description.toLowerCase();
        String normalizedBrand = brand.getName().toLowerCase();

        if (normalizedDesc.contains(normalizedBrand)) {
            signals.add(new DetectionSignal(
                "APP_DESCRIPTION_BRAND_MENTION",
                DetectionSignal.Category.DESCRIPTION_SIMILARITY.name(),
                truncate(description, 200),
                brand.getName(),
                String.format("App description mentions brand name '%s'", brand.getName()),
                8,
                DetectionSignal.Confidence.LOW
            ));
        }
    }

    private void analyzeAppLinks(List<DetectionSignal> signals, String links, Brand brand) {
        if (brand.getWebsite() != null && !brand.getWebsite().isBlank()) {
            String officialDomain = brand.getWebsite().toLowerCase()
                .replaceFirst("^https?://", "").replaceFirst("^www\\.", "");
            if (links.toLowerCase().contains(officialDomain)) return;
        }

        List<String> suspiciousPatterns = List.of("bit.ly", "tinyurl", ".tk", ".ml", "free", "login");
        for (String pattern : suspiciousPatterns) {
            if (links.toLowerCase().contains(pattern)) {
                signals.add(new DetectionSignal(
                    "APP_SUSPICIOUS_LINK",
                    DetectionSignal.Category.SUSPICIOUS_LINK.name(),
                    truncate(links, 200),
                    "Official domain",
                    String.format("App listing contains suspicious link pattern '%s'", pattern),
                    12,
                    DetectionSignal.Confidence.MEDIUM
                ));
                break;
            }
        }
    }

    private String truncate(String text, int maxLen) {
        if (text == null) return "";
        return text.length() <= maxLen ? text : text.substring(0, maxLen) + "...";
    }
}
