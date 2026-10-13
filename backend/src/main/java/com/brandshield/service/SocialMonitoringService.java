package com.brandshield.service;

import com.brandshield.detection.*;
import com.brandshield.dto.response.ScanResultResponse;
import com.brandshield.dto.response.SocialCandidateResponse;
import com.brandshield.dto.request.SocialCandidateCreateRequest;
import com.brandshield.entity.*;
import com.brandshield.mapper.EntityMapper;
import com.brandshield.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocialMonitoringService {

    private static final Logger log = LoggerFactory.getLogger(SocialMonitoringService.class);

    private final SocialCandidateRepository socialCandidateRepository;
    private final BrandRepository brandRepository;
    private final BrandAssetRepository brandAssetRepository;
    private final ThreatDetectionRepository detectionRepository;
    private final SocialCandidateAnalyzer socialAnalyzer;
    private final OfficialAssetVerifier assetVerifier;
    private final RiskScorer riskScorer;

    public SocialMonitoringService(
            SocialCandidateRepository socialCandidateRepository,
            BrandRepository brandRepository,
            BrandAssetRepository brandAssetRepository,
            ThreatDetectionRepository detectionRepository,
            SocialCandidateAnalyzer socialAnalyzer,
            OfficialAssetVerifier assetVerifier,
            RiskScorer riskScorer) {
        this.socialCandidateRepository = socialCandidateRepository;
        this.brandRepository = brandRepository;
        this.brandAssetRepository = brandAssetRepository;
        this.detectionRepository = detectionRepository;
        this.socialAnalyzer = socialAnalyzer;
        this.assetVerifier = assetVerifier;
        this.riskScorer = riskScorer;
    }

    @Transactional(readOnly = true)
    public List<SocialCandidateResponse> getCandidates() {
        return socialCandidateRepository.findAll().stream()
                .map(EntityMapper::toSocialCandidateResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SocialCandidateResponse addCandidate(SocialCandidateCreateRequest request) {
        SocialCandidate candidate = SocialCandidate.builder()
                .platform(request.platform().trim())
                .username(request.username())
                .displayName(request.displayName())
                .accountId(request.accountId())
                .profileUrl(request.profileUrl())
                .description(request.description())
                .avatarUrl(request.avatarUrl())
                .externalLinks(request.externalLinks())
                .followerCount(request.followerCount())
                .isSynthetic(false)
                .source("MANUAL_IMPORT")
                .build();
        return EntityMapper.toSocialCandidateResponse(socialCandidateRepository.save(candidate));
    }

    @Transactional
    public ScanResultResponse scanAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        List<SocialCandidate> candidates = socialCandidateRepository.findAll();

        long startTime = System.currentTimeMillis();
        int detectionsCreated = 0;
        int officialExcluded = 0;

        for (Brand brand : brands) {
            List<BrandAsset> officialAssets = brandAssetRepository.findByBrandId(brand.getId());

            for (SocialCandidate candidate : candidates) {
                OfficialAssetVerifier.VerificationResult verification =
                    assetVerifier.verifySocialCandidate(candidate, officialAssets);
                if (verification.isOfficial()) {
                    officialExcluded++;
                    continue;
                }

                // Check if detection already exists for this candidate-brand pair
                List<ThreatDetection> existing = detectionRepository
                    .findBySocialCandidateAndBrand(candidate.getId(), brand.getId());
                if (!existing.isEmpty()) continue;

                // Analyze the candidate
                List<DetectionSignal> signals = socialAnalyzer.analyze(candidate, brand);

                if (signals.isEmpty()) continue;

                // Calculate risk score
                RiskScorer.RiskAssessment assessment =
                    riskScorer.calculateRiskScore(signals, verification.isOfficial());

                // Only create detection if there are signals or it's official
                if (!signals.isEmpty()) {
                    ThreatDetection detection = createDetection(
                        brand, candidate, signals, assessment, verification);
                    detectionRepository.save(detection);
                    detectionsCreated++;
                }
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        log.info("Social scan completed: {} candidates x {} brands, {} detections created, {} official excluded",
            candidates.size(), brands.size(), detectionsCreated, officialExcluded);

        return ScanResultResponse.builder()
                .candidatesScanned(candidates.size())
                .detectionsCreated(detectionsCreated)
                .officialAssetsExcluded(officialExcluded)
                .scanDuration(duration + "ms")
                .build();
    }

    private ThreatDetection createDetection(Brand brand, SocialCandidate candidate,
                                             List<DetectionSignal> signals,
                                             RiskScorer.RiskAssessment assessment,
                                             OfficialAssetVerifier.VerificationResult verification) {
        // Determine detection type based on signals
        ThreatDetection.DetectionType detectionType = determineDetectionType(signals);

        String summary;
        if (verification.isOfficial()) {
            summary = String.format("Verified official account: %s (matched by %s)",
                candidate.getUsername(), verification.matchType());
        } else {
            summary = String.format("Potential %s detected: %s on %s (risk score: %d)",
                detectionType.name().toLowerCase().replace("_", " "),
                candidate.getDisplayName() != null ? candidate.getDisplayName() : candidate.getUsername(),
                candidate.getPlatform(), assessment.score());
        }

        ThreatDetection detection = ThreatDetection.builder()
                .brand(brand)
                .detectionType(detectionType)
                .candidateType(ThreatDetection.CandidateType.SOCIAL)
                .socialCandidate(candidate)
                .riskScore(assessment.score())
                .riskLevel(assessment.riskLevel())
                .status(verification.isOfficial()
                    ? ThreatDetection.DetectionStatus.LEGITIMATE
                    : ThreatDetection.DetectionStatus.NEW)
                .isOfficialAsset(verification.isOfficial())
                .summary(summary)
                .build();

        // Add evidence records
        for (DetectionSignal signal : signals) {
            DetectionEvidence evidence = DetectionEvidence.builder()
                    .signalName(signal.signalName())
                    .signalCategory(signal.signalCategory())
                    .observedValue(signal.observedValue())
                    .expectedValue(signal.expectedValue())
                    .evidenceExplanation(signal.explanation())
                    .scoreContribution(signal.scoreContribution())
                    .confidence(signal.confidence().name())
                    .build();
            detection.addEvidence(evidence);
        }

        return detection;
    }

    private ThreatDetection.DetectionType determineDetectionType(List<DetectionSignal> signals) {
        boolean hasCharSub = signals.stream()
            .anyMatch(s -> s.signalCategory().equals(DetectionSignal.Category.CHARACTER_SUBSTITUTION.name()));
        boolean hasSuspiciousLink = signals.stream()
            .anyMatch(s -> s.signalCategory().equals(DetectionSignal.Category.SUSPICIOUS_LINK.name()));
        boolean hasHighNameSim = signals.stream()
            .anyMatch(s -> s.signalCategory().equals(DetectionSignal.Category.NAME_SIMILARITY.name())
                && s.scoreContribution() >= 25);

        if (hasSuspiciousLink) return ThreatDetection.DetectionType.PHISHING_INDICATOR;
        if (hasCharSub) return ThreatDetection.DetectionType.BRAND_IMPERSONATION;
        if (hasHighNameSim) return ThreatDetection.DetectionType.LOOK_ALIKE;
        return ThreatDetection.DetectionType.SUSPICIOUS_CONTENT;
    }
}
