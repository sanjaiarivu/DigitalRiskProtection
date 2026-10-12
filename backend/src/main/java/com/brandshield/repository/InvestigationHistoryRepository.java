package com.brandshield.repository;

import com.brandshield.entity.InvestigationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvestigationHistoryRepository extends JpaRepository<InvestigationHistory, Long> {
    List<InvestigationHistory> findByDetectionIdOrderByChangedAtDesc(Long detectionId);
}
