package com.brandshield.detection;

import com.brandshield.entity.Brand;
import com.brandshield.entity.BrandAlias;
import com.brandshield.entity.SocialCandidate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Analyzes social media candidate profiles for brand impersonation signals.
 * Produces a list of detection signals that feed into the risk scorer.
 */
@Component
public class SocialCandidateAnalyzer {

    private final TextSimilarityAnalyzer textAnalyzer;

    /** Minimum similarity threshold to generate a signal. Below this, the name is 
     * considered too different to be suspicious. Set conservatively to reduce noise. */
    private static final double SIMILARITY_THRESHOLD = 0.5;

    /** Suspicious URL patterns commonly associated with phishing. */
    private static final List<String> SUSPICIOUS_URL_PATTERNS = List.of(
        "bit.ly", "tinyurl.com", "t.co", "goo.gl", "rebrand.ly",
        "linktr.ee", "discord.gg", "telegram.me", ".tk", ".ml",
        ".ga", ".cf", "free", "login", "verify", "secure",
        "account", "password", "signin"
    );

    public SocialCandidateAnalyzer(TextSimilarityAnalyzer textAnalyzer) {
        this.textAnalyzer = textAnalyzer;
    }

    /**
     * Analyzes a social candidate against a brand and its aliases.
     * Returns all detected signals.
     */
    public List<DetectionSignal> analyze(SocialCandidate candidate, Brand brand) {
        List<DetectionSignal> signals = new ArrayList<>();

        // Collect all names to check against (brand name + aliases)
        List<String> brandNames = new ArrayList<>();
        brandNames.add(brand.getName());
        if (brand.getAliases() != null) {
            brand.getAliases().stream()
                .map(BrandAlias::getAliasName)
                .forEach(brandNames::add);
        }

        // Analyze username similarity against each brand name
        for (String brandName : brandNames) {
            if (candidate.getUsername() != null) {
                analyzeNameSignals(signals, brandName, candidate.getUsername(), "USERNAME");
            }
            if (candidate.getDisplayName() != null) {
                analyzeNameSignals(signals, brandName, candidate.getDisplayName(), "DISPLAY_NAME");
            }
        }

        // Analyze description for brand mentions or keywords
        if (candidate.getDescription() != null && !candidate.getDescription().isBlank()) {
            analyzeDescription(signals, candidate.getDescription(), brand);
        }

        // Analyze external links
        if (candidate.getExternalLinks() != null && !candidate.getExternalLinks().isBlank()) {
            analyzeExternalLinks(signals, candidate.getExternalLinks(), brand);
        }

        return signals;
    }

    private void analyzeNameSignals(List<DetectionSignal> signals, String brandName,
                                     String candidateName, String nameType) {
        TextSimilarityAnalyzer.NameSimilarityResult result =
            textAnalyzer.analyzeNameSimilarity(brandName, candidateName);

        // Only generate signals if similarity exceeds threshold
        if (result.overallSimilarityScore() < SIMILARITY_THRESHOLD) return;

        // High similarity signal
        if (result.normalizedSimilarity() >= 0.7) {
            int contribution = (int) (result.normalizedSimilarity() * 30);
            signals.add(new DetectionSignal(
                nameType + "_SIMILARITY",
                DetectionSignal.Category.NAME_SIMILARITY.name(),
                candidateName,
                brandName,
                String.format("%s '%s' has %.0f%% similarity to brand name '%s' (edit distance: %d)",
                    nameType.toLowerCase().replace("_", " "), candidateName,
                    result.normalizedSimilarity() * 100, brandName, result.editDistance()),
                contribution,
                result.normalizedSimilarity() >= 0.9
                    ? DetectionSignal.Confidence.HIGH
                    : DetectionSignal.Confidence.MEDIUM
            ));
        }

        // Character substitution signals
        if (result.hasCharSubstitutions()) {
            Map<Integer, String> subs = result.substitutionDetails();
            signals.add(new DetectionSignal(
                nameType + "_CHAR_SUBSTITUTION",
                DetectionSignal.Category.CHARACTER_SUBSTITUTION.name(),
                candidateName,
                brandName,
                String.format("Detected %d look-alike character substitution(s): %s",
                    subs.size(), String.join("; ", subs.values())),
                20,
                DetectionSignal.Confidence.HIGH
            ));
        }

        // Affix pattern signals
        if (result.hasAddedAffixes()) {
            TextSimilarityAnalyzer.AffixResult affix = result.affixDetails();
            String affixDesc = affix.affixType().equals("unknown_affix")
                ? "added text '" + affix.affixValue() + "'"
                : affix.affixType() + " '" + affix.affixValue() + "'";
            signals.add(new DetectionSignal(
                nameType + "_AFFIX_PATTERN",
                DetectionSignal.Category.AFFIX_PATTERN.name(),
                candidateName,
                brandName,
                String.format("%s contains brand name with %s — a common impersonation pattern",
                    nameType.toLowerCase().replace("_", " "), affixDesc),
                15,
                "unknown_affix".equals(affix.affixType())
                    ? DetectionSignal.Confidence.LOW
                    : DetectionSignal.Confidence.MEDIUM
            ));
        }
    }

    private void analyzeDescription(List<DetectionSignal> signals, String description, Brand brand) {
        String normalizedDesc = description.toLowerCase();
        String normalizedBrand = brand.getName().toLowerCase();

        if (normalizedDesc.contains(normalizedBrand)) {
            signals.add(new DetectionSignal(
                "DESCRIPTION_BRAND_MENTION",
                DetectionSignal.Category.DESCRIPTION_SIMILARITY.name(),
                truncate(description, 200),
                brand.getName(),
                String.format("Profile description mentions brand name '%s'", brand.getName()),
                10,
                DetectionSignal.Confidence.LOW
            ));
        }
    }

    private void analyzeExternalLinks(List<DetectionSignal> signals, String links, Brand brand) {
        String normalizedLinks = links.toLowerCase();

        // Check if links point to the official domain (reduces false positives)
        if (brand.getWebsite() != null && !brand.getWebsite().isBlank()) {
            String officialDomain = extractDomain(brand.getWebsite());
            if (!officialDomain.isEmpty() && normalizedLinks.contains(officialDomain)) {
                return; // Link points to official domain, not suspicious
            }
        }

        for (String pattern : SUSPICIOUS_URL_PATTERNS) {
            if (normalizedLinks.contains(pattern)) {
                signals.add(new DetectionSignal(
                    "SUSPICIOUS_EXTERNAL_LINK",
                    DetectionSignal.Category.SUSPICIOUS_LINK.name(),
                    truncate(links, 200),
                    "Official domain",
                    String.format("External link contains suspicious pattern '%s'", pattern),
                    15,
                    DetectionSignal.Confidence.MEDIUM
                ));
                break; // One signal per link analysis is sufficient
            }
        }
    }

    private String extractDomain(String url) {
        if (url == null) return "";
        String domain = url.toLowerCase().replaceFirst("^https?://", "").replaceFirst("^www\\.", "");
        int slashIdx = domain.indexOf('/');
        if (slashIdx > 0) domain = domain.substring(0, slashIdx);
        return domain;
    }

    private String truncate(String text, int maxLen) {
        if (text == null) return "";
        return text.length() <= maxLen ? text : text.substring(0, maxLen) + "...";
    }
}
