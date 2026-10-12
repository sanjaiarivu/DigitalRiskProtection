package com.brandshield.service;

import com.brandshield.detection.*;
import com.brandshield.dto.response.AppCandidateResponse;
import com.brandshield.dto.response.ScanResultResponse;
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
public class AppMonitoringService {

    private static final Logger log = LoggerFactory.getLogger(AppMonitoringService.class);

    private final AppCandidateRepository appCandidateRepository;
    private final BrandRepository brandRepository;
    private final BrandAssetRepository brandAssetRepository;
    private final ThreatDetectionRepository detectionRepository;
    private final AppCandidateAnalyzer appAnalyzer;
    private final OfficialAssetVerifier assetVerifier;
    private final RiskScorer riskScorer;

    public AppMonitoringService(
            AppCandidateRepository appCandidateRepository,
            BrandRepository brandRepository,
            BrandAssetRepository brandAssetRepository,
            ThreatDetectionRepository detectionRepository,
            AppCandidateAnalyzer appAnalyzer,
            OfficialAssetVerifier assetVerifier,
            RiskScorer riskScorer) {
        this.appCandidateRepository = appCandidateRepository;
        this.brandRepository = brandRepository;
        this.brandAssetRepository = brandAssetRepository;
        this.detectionRepository = detectionRepository;
        this.appAnalyzer = appAnalyzer;
        this.assetVerifier = assetVerifier;
        this.riskScorer = riskScorer;
    }

    @Transactional(readOnly = true)
    public List<AppCandidateResponse> getCandidates() {
        return appCandidateRepository.findAll().stream()
                .map(EntityMapper::toAppCandidateResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ScanResultResponse scanAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        List<AppCandidate> candidates = appCandidateRepository.findAll();

        long startTime = System.currentTimeMillis();
        int detectionsCreated = 0;
        int officialExcluded = 0;

        for (Brand brand : brands) {
            List<BrandAsset> officialAssets = brandAssetRepository.findByBrandId(brand.getId());

            for (AppCandidate candidate : candidates) {
                List<ThreatDetection> existing = detectionRepository
                    .findByAppCandidateAndBrand(candidate.getId(), brand.getId());
                if (!existing.isEmpty()) continue;

                OfficialAssetVerifier.VerificationResult verification =
                    assetVerifier.verifyAppCandidate(candidate, officialAssets);

                List<DetectionSignal> signals = appAnalyzer.analyze(candidate, brand, officialAssets);

                if (signals.isEmpty() && !verification.isOfficial()) continue;

                RiskScorer.RiskAssessment assessment =
                    riskScorer.calculateRiskScore(signals, verification.isOfficial());

                if (verification.isOfficial()) {
                    officialExcluded++;
                }

                if (!signals.isEmpty() || verification.isOfficial()) {
                    ThreatDetection detection = createDetection(
                        brand, candidate, signals, assessment, verification);
                    detectionRepository.save(detection);
                    detectionsCreated++;
                }
            }
        }

        long duration = System.currentTimeMillis() - startTime;
        log.info("App scan completed: {} candidates x {} brands, {} detections, {} official excluded",
            candidates.size(), brands.size(), detectionsCreated, officialExcluded);

        return ScanResultResponse.builder()
                .candidatesScanned(candidates.size())
                .detectionsCreated(detectionsCreated)
                .officialAssetsExcluded(officialExcluded)
                .scanDuration(duration + "ms")
                .build();
    }

    private ThreatDetection createDetection(Brand brand, AppCandidate candidate,
                                             List<DetectionSignal> signals,
                                             RiskScorer.RiskAssessment assessment,
                                             OfficialAssetVerifier.VerificationResult verification) {
        ThreatDetection.DetectionType detectionType = determineDetectionType(signals);

        String summary;
        if (verification.isOfficial()) {
            summary = String.format("Verified official app: %s (matched by %s)",
                candidate.getAppName(), verification.matchType());
        } else {
            summary = String.format("Potential %s detected: app '%s' by '%s' (risk score: %d)",
                detectionType.name().toLowerCase().replace("_", " "),
                candidate.getAppName(), candidate.getPublisherName(), assessment.score());
        }

        ThreatDetection detection = ThreatDetection.builder()
                .brand(brand)
                .detectionType(detectionType)
                .candidateType(ThreatDetection.CandidateType.APP)
                .appCandidate(candidate)
                .riskScore(assessment.score())
                .riskLevel(assessment.riskLevel())
                .status(verification.isOfficial()
                    ? ThreatDetection.DetectionStatus.LEGITIMATE
                    : ThreatDetection.DetectionStatus.NEW)
                .isOfficialAsset(verification.isOfficial())
                .summary(summary)
                .build();

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
        boolean hasPublisherMismatch = signals.stream()
            .anyMatch(s -> s.signalCategory().equals(DetectionSignal.Category.PUBLISHER_MISMATCH.name()));
        boolean hasIdConflict = signals.stream()
            .anyMatch(s -> s.signalCategory().equals(DetectionSignal.Category.IDENTIFIER_CONFLICT.name()));
        boolean hasCharSub = signals.stream()
            .anyMatch(s -> s.signalCategory().equals(DetectionSignal.Category.CHARACTER_SUBSTITUTION.name()));

        if (hasIdConflict) return ThreatDetection.DetectionType.BRAND_IMPERSONATION;
        if (hasPublisherMismatch) return ThreatDetection.DetectionType.PUBLISHER_MISMATCH;
        if (hasCharSub) return ThreatDetection.DetectionType.BRAND_IMPERSONATION;
        return ThreatDetection.DetectionType.LOOK_ALIKE;
    }
}
