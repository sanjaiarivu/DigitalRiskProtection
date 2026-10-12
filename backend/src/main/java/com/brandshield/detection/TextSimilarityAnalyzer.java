package com.brandshield.detection;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Analyzes text similarity between brand names and candidate names.
 *
 * Uses multiple complementary techniques:
 * 1. Edit distance (Levenshtein) - captures insertions/deletions/substitutions
 * 2. Token-based Jaccard similarity - handles word reordering
 * 3. Character substitution detection - catches deliberate look-alike replacements
 * 4. Affix detection - catches added prefixes/suffixes like _official, _support
 *
 * Each technique captures a different type of impersonation attempt,
 * so using them together reduces false negatives without significantly
 * increasing false positives.
 */
@Component
public class TextSimilarityAnalyzer {

    private static final LevenshteinDistance LEVENSHTEIN = LevenshteinDistance.getDefaultInstance();
    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-z0-9\\s]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");

    /**
     * Common look-alike character substitutions used in impersonation.
     * Maps the substitute character to what it visually resembles.
     */
    private static final Map<Character, Character> LOOK_ALIKE_CHARS = Map.ofEntries(
        Map.entry('0', 'o'),
        Map.entry('1', 'l'),
        Map.entry('3', 'e'),
        Map.entry('4', 'a'),
        Map.entry('5', 's'),
        Map.entry('8', 'b'),
        Map.entry('@', 'a'),
        Map.entry('!', 'i'),
        Map.entry('|', 'l')
    );

    /**
     * Common impersonation affixes added to brand names.
     */
    private static final List<String> SUSPICIOUS_PREFIXES = List.of(
        "official", "real", "the", "original", "verified", "true"
    );

    private static final List<String> SUSPICIOUS_SUFFIXES = List.of(
        "official", "real", "support", "help", "helpdesk", "care",
        "service", "team", "hq", "inc", "app", "verify"
    );

    /**
     * Normalizes text for comparison: Unicode NFKD normalization,
     * lowercase, remove non-alphanumeric, collapse whitespace.
     */
    public String normalizeText(String text) {
        if (text == null || text.isBlank()) return "";
        String normalized = Normalizer.normalize(text.trim(), Normalizer.Form.NFKD);
        normalized = normalized.toLowerCase();
        normalized = NON_ALPHANUMERIC.matcher(normalized).replaceAll("");
        normalized = WHITESPACE.matcher(normalized).replaceAll(" ").trim();
        return normalized;
    }

    /**
     * Calculates Levenshtein edit distance between two strings.
     */
    public int calculateEditDistance(String a, String b) {
        return LEVENSHTEIN.apply(normalizeText(a), normalizeText(b));
    }

    /**
     * Calculates normalized similarity: 1 - (editDistance / maxLength).
     * Returns a value between 0.0 (completely different) and 1.0 (identical).
     */
    public double calculateNormalizedSimilarity(String a, String b) {
        String na = normalizeText(a);
        String nb = normalizeText(b);
        if (na.isEmpty() && nb.isEmpty()) return 1.0;
        if (na.isEmpty() || nb.isEmpty()) return 0.0;
        int distance = LEVENSHTEIN.apply(na, nb);
        int maxLen = Math.max(na.length(), nb.length());
        return 1.0 - ((double) distance / maxLen);
    }

    /**
     * Calculates Jaccard similarity based on word tokens.
     * Handles cases where words appear in different order.
     */
    public double calculateTokenSimilarity(String a, String b) {
        Set<String> tokensA = tokenize(a);
        Set<String> tokensB = tokenize(b);
        if (tokensA.isEmpty() && tokensB.isEmpty()) return 1.0;
        if (tokensA.isEmpty() || tokensB.isEmpty()) return 0.0;

        Set<String> intersection = new HashSet<>(tokensA);
        intersection.retainAll(tokensB);

        Set<String> union = new HashSet<>(tokensA);
        union.addAll(tokensB);

        return (double) intersection.size() / union.size();
    }

    /**
     * Detects character substitutions that make names look alike.
     * Returns a map of position -> description of substitution.
     */
    public Map<Integer, String> detectCharacterSubstitutions(String brandName, String candidateName) {
        Map<Integer, String> substitutions = new LinkedHashMap<>();
        String nb = normalizeText(brandName);
        String nc = normalizeText(candidateName);

        int minLen = Math.min(nb.length(), nc.length());
        for (int i = 0; i < minLen; i++) {
            char bc = nb.charAt(i);
            char cc = nc.charAt(i);
            if (bc != cc) {
                Character looksLike = LOOK_ALIKE_CHARS.get(cc);
                if (looksLike != null && looksLike == bc) {
                    substitutions.put(i, "'" + cc + "' substituted for '" + bc + "' at position " + i);
                }
            }
        }
        return substitutions;
    }

    /**
     * Detects if the candidate name has suspicious prefixes or suffixes
     * added to the brand name (e.g., "techvault_support", "official_techvault").
     */
    public AffixResult detectAddedPrefixSuffix(String brandName, String candidateName) {
        String nb = normalizeText(brandName);
        String nc = normalizeText(candidateName);

        if (nb.isEmpty() || nc.isEmpty() || nb.equals(nc)) {
            return new AffixResult(false, null, null);
        }

        // Check if candidate contains the brand name with something added
        if (nc.contains(nb)) {
            String before = nc.substring(0, nc.indexOf(nb));
            String after = nc.substring(nc.indexOf(nb) + nb.length());

            for (String prefix : SUSPICIOUS_PREFIXES) {
                if (before.contains(prefix)) {
                    return new AffixResult(true, "prefix", prefix);
                }
            }
            for (String suffix : SUSPICIOUS_SUFFIXES) {
                if (after.contains(suffix)) {
                    return new AffixResult(true, "suffix", suffix);
                }
            }

            // Generic added text (not a known suspicious pattern)
            if (!before.isBlank() || !after.isBlank()) {
                String addedText = (before + after).trim();
                if (!addedText.isEmpty() && addedText.length() <= nb.length()) {
                    return new AffixResult(true, "unknown_affix", addedText);
                }
            }
        }

        return new AffixResult(false, null, null);
    }

    /**
     * Performs comprehensive name similarity analysis.
     */
    public NameSimilarityResult analyzeNameSimilarity(String brandName, String candidateName) {
        double normalizedSimilarity = calculateNormalizedSimilarity(brandName, candidateName);
        int editDistance = calculateEditDistance(brandName, candidateName);
        double tokenSimilarity = calculateTokenSimilarity(brandName, candidateName);
        Map<Integer, String> substitutions = detectCharacterSubstitutions(brandName, candidateName);
        AffixResult affixResult = detectAddedPrefixSuffix(brandName, candidateName);

        Set<String> tokensA = tokenize(brandName);
        Set<String> tokensB = tokenize(candidateName);

        // For single-word names, character-level similarity is the authoritative measure.
        // Token Jaccard is only blended for multi-word phrases.
        double overallScore;
        if (tokensA.size() <= 1 && tokensB.size() <= 1) {
            overallScore = normalizedSimilarity;
        } else {
            overallScore = normalizedSimilarity * 0.7 + tokenSimilarity * 0.3;
        }

        // Boost score if character substitutions detected (strong impersonation signal)
        if (!substitutions.isEmpty()) {
            overallScore = Math.min(1.0, overallScore + 0.15);
        }

        // Boost score if suspicious affix detected
        if (affixResult.hasAffix() && affixResult.affixType() != null
            && !"unknown_affix".equals(affixResult.affixType())) {
            overallScore = Math.min(1.0, overallScore + 0.1);
        }

        return new NameSimilarityResult(
            normalizedSimilarity, editDistance, tokenSimilarity,
            !substitutions.isEmpty(), substitutions,
            affixResult.hasAffix(), affixResult,
            overallScore
        );
    }

    private Set<String> tokenize(String text) {
        String normalized = normalizeText(text);
        if (normalized.isEmpty()) return Set.of();
        return Arrays.stream(normalized.split("\\s+"))
            .filter(s -> !s.isEmpty())
            .collect(Collectors.toSet());
    }

    public record NameSimilarityResult(
        double normalizedSimilarity,
        int editDistance,
        double tokenSimilarity,
        boolean hasCharSubstitutions,
        Map<Integer, String> substitutionDetails,
        boolean hasAddedAffixes,
        AffixResult affixDetails,
        double overallSimilarityScore
    ) {}

    public record AffixResult(
        boolean hasAffix,
        String affixType,
        String affixValue
    ) {}
}
