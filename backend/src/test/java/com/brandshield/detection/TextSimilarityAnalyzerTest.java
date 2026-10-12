package com.brandshield.detection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextSimilarityAnalyzerTest {

    private TextSimilarityAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        analyzer = new TextSimilarityAnalyzer();
    }

    @Test
    @DisplayName("Exact match should produce 1.0 normalized similarity and 0 edit distance")
    void testExactMatch() {
        TextSimilarityAnalyzer.NameSimilarityResult result = analyzer.analyzeNameSimilarity("TechVault", "TechVault");
        assertEquals(1.0, result.normalizedSimilarity(), 0.001);
        assertEquals(0, result.editDistance());
        assertFalse(result.hasCharSubstitutions());
    }

    @Test
    @DisplayName("Case and whitespace differences should be normalized")
    void testNormalization() {
        double sim = analyzer.calculateNormalizedSimilarity("  TechVault  ", "techvault");
        assertEquals(1.0, sim, 0.001);
    }

    @Test
    @DisplayName("Character substitution: 1 for l in 'techvau1t' should be detected")
    void testCharacterSubstitution_1ForL() {
        TextSimilarityAnalyzer.NameSimilarityResult result = analyzer.analyzeNameSimilarity("techvault", "techvau1t");
        assertTrue(result.hasCharSubstitutions(), "Should detect '1' substituted for 'l'");
        assertTrue(result.substitutionDetails().containsKey(7));
        assertTrue(result.overallSimilarityScore() > 0.85);
    }

    @Test
    @DisplayName("Character substitution: 0 for o in 'g00gle' should be detected")
    void testCharacterSubstitution_0ForO() {
        TextSimilarityAnalyzer.NameSimilarityResult result = analyzer.analyzeNameSimilarity("google", "g00gle");
        assertTrue(result.hasCharSubstitutions());
        assertEquals(2, result.substitutionDetails().size());
    }

    @Test
    @DisplayName("Suspicious suffix: 'techvault_support' should be detected")
    void testSuspiciousSuffix() {
        TextSimilarityAnalyzer.NameSimilarityResult result = analyzer.analyzeNameSimilarity("techvault", "techvault_support");
        assertTrue(result.hasAddedAffixes());
        assertEquals("suffix", result.affixDetails().affixType());
        assertEquals("support", result.affixDetails().affixValue());
    }

    @Test
    @DisplayName("Suspicious prefix: 'official_techvault' should be detected")
    void testSuspiciousPrefix() {
        TextSimilarityAnalyzer.NameSimilarityResult result = analyzer.analyzeNameSimilarity("techvault", "official_techvault");
        assertTrue(result.hasAddedAffixes());
        assertEquals("prefix", result.affixDetails().affixType());
        assertEquals("official", result.affixDetails().affixValue());
    }

    @Test
    @DisplayName("Completely unrelated names should have low similarity")
    void testUnrelatedNames() {
        TextSimilarityAnalyzer.NameSimilarityResult result = analyzer.analyzeNameSimilarity("TechVault", "GreenForestGarden");
        assertTrue(result.normalizedSimilarity() < 0.3);
        assertFalse(result.hasCharSubstitutions());
    }

    @Test
    @DisplayName("Token similarity handles word reordering")
    void testTokenSimilarity() {
        double tokenSim = analyzer.calculateTokenSimilarity("Vault Tech Secure", "Secure Tech Vault");
        assertEquals(1.0, tokenSim, 0.001);
    }
}
