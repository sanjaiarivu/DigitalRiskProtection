package com.brandshield.repository;

import com.brandshield.entity.InvestigationNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvestigationNoteRepository extends JpaRepository<InvestigationNote, Long> {
    List<InvestigationNote> findByDetectionIdOrderByCreatedAtDesc(Long detectionId);
}
