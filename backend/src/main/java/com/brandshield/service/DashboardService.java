package com.brandshield.service;

import com.brandshield.dto.response.DashboardSummaryResponse;
import com.brandshield.dto.response.ThreatDetectionResponse;
import com.brandshield.entity.ThreatDetection;
import com.brandshield.mapper.EntityMapper;
import com.brandshield.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final BrandRepository brandRepository;
    private final SocialCandidateRepository socialCandidateRepository;
    private final AppCandidateRepository appCandidateRepository;
    private final ThreatDetectionRepository detectionRepository;

    public DashboardService(BrandRepository brandRepository,
                           SocialCandidateRepository socialCandidateRepository,
                           AppCandidateRepository appCandidateRepository,
                           ThreatDetectionRepository detectionRepository) {
        this.brandRepository = brandRepository;
        this.socialCandidateRepository = socialCandidateRepository;
        this.appCandidateRepository = appCandidateRepository;
        this.detectionRepository = detectionRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary() {
        long totalBrands = brandRepository.count();
        long totalSocial = socialCandidateRepository.count();
        long totalApps = appCandidateRepository.count();
        long totalCandidates = totalSocial + totalApps;
        long totalDetections = detectionRepository.countThreatDetections();
        long newDetections = detectionRepository.countByStatus(ThreatDetection.DetectionStatus.NEW);
        long highRisk = detectionRepository.countByRiskLevel(ThreatDetection.RiskLevel.HIGH);
        long critical = detectionRepository.countByRiskLevel(ThreatDetection.RiskLevel.CRITICAL);

        // Risk distribution
        Map<String, Long> riskDistribution = new LinkedHashMap<>();
        for (ThreatDetection.RiskLevel level : ThreatDetection.RiskLevel.values()) {
            riskDistribution.put(level.name(), detectionRepository.countByRiskLevel(level));
        }

        // Detections by candidate type
        Map<String, Long> detectionsByType = new LinkedHashMap<>();
        detectionsByType.put("SOCIAL", detectionRepository.countByCandidateType(ThreatDetection.CandidateType.SOCIAL));
        detectionsByType.put("APP", detectionRepository.countByCandidateType(ThreatDetection.CandidateType.APP));

        // Recent detections
        List<ThreatDetectionResponse> recentDetections = detectionRepository.findRecentThreats().stream().limit(10)
                .map(EntityMapper::toThreatDetectionResponse)
                .collect(Collectors.toList());

        return DashboardSummaryResponse.builder()
                .totalBrands(totalBrands)
                .totalCandidates(totalCandidates)
                .totalDetections(totalDetections)
                .newDetections(newDetections)
                .highRiskDetections(highRisk)
                .criticalDetections(critical)
                .riskDistribution(riskDistribution)
                .detectionsByType(detectionsByType)
                .recentDetections(recentDetections)
                .build();
    }
}
