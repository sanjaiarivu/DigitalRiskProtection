package com.brandshield.repository;

import com.brandshield.entity.ThreatDetection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ThreatDetectionRepository extends JpaRepository<ThreatDetection, Long> {
    List<ThreatDetection> findByBrandId(Long brandId);
    List<ThreatDetection> findByStatus(ThreatDetection.DetectionStatus status);
    List<ThreatDetection> findByCandidateType(ThreatDetection.CandidateType type);
    List<ThreatDetection> findByRiskLevel(ThreatDetection.RiskLevel level);
    long countByStatus(ThreatDetection.DetectionStatus status);
    long countByRiskLevel(ThreatDetection.RiskLevel level);
    long countByCandidateType(ThreatDetection.CandidateType type);
    long countByRiskLevelIn(List<ThreatDetection.RiskLevel> levels);

    @Query("SELECT d FROM ThreatDetection d WHERE " +
           "(:brandId IS NULL OR d.brand.id = :brandId) AND " +
           "(:status IS NULL OR d.status = :status) AND " +
           "(:riskLevel IS NULL OR d.riskLevel = :riskLevel) AND " +
           "(:candidateType IS NULL OR d.candidateType = :candidateType) " +
           "ORDER BY d.detectedAt DESC")
    List<ThreatDetection> findFiltered(
        @Param("brandId") Long brandId,
        @Param("status") ThreatDetection.DetectionStatus status,
        @Param("riskLevel") ThreatDetection.RiskLevel riskLevel,
        @Param("candidateType") ThreatDetection.CandidateType candidateType
    );

    List<ThreatDetection> findTop10ByOrderByDetectedAtDesc();

    @Query("SELECT d FROM ThreatDetection d WHERE d.socialCandidate.id = :candidateId AND d.brand.id = :brandId")
    List<ThreatDetection> findBySocialCandidateAndBrand(@Param("candidateId") Long candidateId, @Param("brandId") Long brandId);

    @Query("SELECT d FROM ThreatDetection d WHERE d.appCandidate.id = :candidateId AND d.brand.id = :brandId")
    List<ThreatDetection> findByAppCandidateAndBrand(@Param("candidateId") Long candidateId, @Param("brandId") Long brandId);
}
