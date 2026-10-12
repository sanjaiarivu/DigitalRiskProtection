package com.brandshield.service;

import com.brandshield.entity.*;
import com.brandshield.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Seeds synthetic demo data for hackathon demonstrations.
 * Only available when app.demo-mode=true.
 * All demo records are marked as synthetic and clearly labeled.
 */
@Service
public class DemoDataService {

    private static final Logger log = LoggerFactory.getLogger(DemoDataService.class);

    @Value("${app.demo-mode:false}")
    private boolean demoMode;

    private final BrandRepository brandRepository;
    private final SocialCandidateRepository socialCandidateRepository;
    private final AppCandidateRepository appCandidateRepository;
    private final ThreatDetectionRepository detectionRepository;

    public DemoDataService(BrandRepository brandRepository,
                          SocialCandidateRepository socialCandidateRepository,
                          AppCandidateRepository appCandidateRepository,
                          ThreatDetectionRepository detectionRepository) {
        this.brandRepository = brandRepository;
        this.socialCandidateRepository = socialCandidateRepository;
        this.appCandidateRepository = appCandidateRepository;
        this.detectionRepository = detectionRepository;
    }

    @Transactional
    public String seedDemoData() {
        if (!demoMode) {
            throw new IllegalStateException("Demo mode is not enabled. Set DEMO_MODE=true to enable.");
        }

        log.info("Seeding demo data...");

        // Create brand: TechVault
        Brand brand = Brand.builder()
                .name("TechVault")
                .description("Leading secure cloud storage and digital vault platform")
                .website("https://techvault.com")
                .logoUrl("https://techvault.com/logo.png")
                .build();

        BrandAlias alias1 = BrandAlias.builder().aliasName("Tech Vault").build();
        BrandAlias alias2 = BrandAlias.builder().aliasName("TV Cloud").build();
        brand.addAlias(alias1);
        brand.addAlias(alias2);

        // Official social account asset
        BrandAsset socialAsset = BrandAsset.builder()
                .assetType(BrandAsset.AssetType.SOCIAL_ACCOUNT)
                .platform("Twitter")
                .identifier("tv_official_001")
                .displayName("TechVault")
                .canonicalUrl("https://twitter.com/techvault")
                .verified(true)
                .build();
        brand.addAsset(socialAsset);

        // Official mobile app asset
        BrandAsset appAsset = BrandAsset.builder()
                .assetType(BrandAsset.AssetType.MOBILE_APP)
                .platform("Google Play")
                .identifier("com.techvault.app")
                .displayName("TechVault Inc")
                .canonicalUrl("https://play.google.com/store/apps/details?id=com.techvault.app")
                .verified(true)
                .build();
        brand.addAsset(appAsset);

        brand = brandRepository.save(brand);
        log.info("Created brand: {} with {} assets", brand.getName(), brand.getAssets().size());

        // --- Social Candidates ---

        // 1. Official account (should be excluded by verifier)
        SocialCandidate officialSocial = SocialCandidate.builder()
                .platform("Twitter")
                .username("techvault")
                .displayName("TechVault")
                .accountId("tv_official_001")
                .profileUrl("https://twitter.com/techvault")
                .description("Official TechVault account. Secure cloud storage for everyone.")
                .followerCount(125000)
                .isSynthetic(true)
                .source("DEMO")
                .build();
        socialCandidateRepository.save(officialSocial);

        // 2. Suspicious look-alike (character substitution: 1 for l)
        SocialCandidate suspiciousSocial1 = SocialCandidate.builder()
                .platform("Twitter")
                .username("techvau1t")
                .displayName("TechVau1t")
                .accountId("fake_tv_001")
                .profileUrl("https://twitter.com/techvau1t")
                .description("TechVault - Your trusted cloud storage. Download our app now!")
                .externalLinks("https://bit.ly/techvault-free")
                .followerCount(342)
                .isSynthetic(true)
                .source("DEMO")
                .build();
        socialCandidateRepository.save(suspiciousSocial1);

        // 3. Suspicious with added suffix
        SocialCandidate suspiciousSocial2 = SocialCandidate.builder()
                .platform("Twitter")
                .username("techvault_support")
                .displayName("TechVault Support")
                .accountId("fake_tv_002")
                .profileUrl("https://twitter.com/techvault_support")
                .description("Official TechVault customer support. DM us for help! Click link for verification.")
                .externalLinks("https://techvau1t-verify.tk/login")
                .followerCount(89)
                .isSynthetic(true)
                .source("DEMO")
                .build();
        socialCandidateRepository.save(suspiciousSocial2);

        // 4. Legitimate unrelated profile
        SocialCandidate legitimateSocial = SocialCandidate.builder()
                .platform("Twitter")
                .username("techvalley")
                .displayName("Tech Valley Hub")
                .accountId("tv_unrelated_001")
                .profileUrl("https://twitter.com/techvalley")
                .description("Technology news and events from Silicon Valley. Not affiliated with any specific company.")
                .externalLinks("https://techvalleyhub.com")
                .followerCount(15600)
                .isSynthetic(true)
                .source("DEMO")
                .build();
        socialCandidateRepository.save(legitimateSocial);

        log.info("Created 4 social candidates (1 official, 2 suspicious, 1 legitimate)");

        // --- App Candidates ---

        // 5. Official app (should be excluded by verifier)
        AppCandidate officialApp = AppCandidate.builder()
                .appName("TechVault - Cloud Storage")
                .packageId("com.techvault.app")
                .publisherName("TechVault Inc")
                .publisherId("pub_tv_001")
                .storePlatform("Google Play")
                .storeUrl("https://play.google.com/store/apps/details?id=com.techvault.app")
                .description("Secure cloud storage with end-to-end encryption. Store, sync, and share your files safely.")
                .downloadCount("5,000,000+")
                .rating(4.5)
                .isSynthetic(true)
                .source("DEMO")
                .build();
        appCandidateRepository.save(officialApp);

        // 6. Suspicious app (similar package ID, different publisher)
        AppCandidate suspiciousApp1 = AppCandidate.builder()
                .appName("TechVau1t Cloud Storage")
                .packageId("com.techvauit.app")
                .publisherName("TechVault Apps")
                .publisherId("pub_fake_001")
                .storePlatform("Google Play")
                .storeUrl("https://play.google.com/store/apps/details?id=com.techvauit.app")
                .description("TechVault cloud storage app - Free unlimited storage! Download now.")
                .externalLinks("https://techvauit-app.ml/download")
                .downloadCount("1,000+")
                .rating(3.2)
                .isSynthetic(true)
                .source("DEMO")
                .build();
        appCandidateRepository.save(suspiciousApp1);

        // 7. Legitimate unrelated app
        AppCandidate legitimateApp = AppCandidate.builder()
                .appName("VaultTech Secure Notes")
                .packageId("com.vaulttech.secure")
                .publisherName("VaultTech LLC")
                .publisherId("pub_vt_001")
                .storePlatform("Google Play")
                .storeUrl("https://play.google.com/store/apps/details?id=com.vaulttech.secure")
                .description("Encrypted note-taking app. Keep your personal notes safe and private.")
                .downloadCount("100,000+")
                .rating(4.1)
                .isSynthetic(true)
                .source("DEMO")
                .build();
        appCandidateRepository.save(legitimateApp);

        log.info("Created 3 app candidates (1 official, 1 suspicious, 1 legitimate)");

        return "Demo data seeded: 1 brand with 2 official assets, 4 social candidates, 3 app candidates. Run scans to generate detections.";
    }

    @Transactional
    public String clearDemoData() {
        if (!demoMode) {
            throw new IllegalStateException("Demo mode is not enabled.");
        }

        // Delete detections first (foreign keys)
        detectionRepository.deleteAll();
        socialCandidateRepository.deleteAll();
        appCandidateRepository.deleteAll();
        brandRepository.deleteAll();

        log.info("Demo data cleared");
        return "All data cleared.";
    }
}
