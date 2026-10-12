package com.brandshield.repository;

import com.brandshield.entity.DetectionEvidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetectionEvidenceRepository extends JpaRepository<DetectionEvidence, Long> {
    List<DetectionEvidence> findByDetectionId(Long detectionId);
}
