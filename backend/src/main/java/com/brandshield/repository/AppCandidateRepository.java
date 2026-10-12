package com.brandshield.repository;

import com.brandshield.entity.AppCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppCandidateRepository extends JpaRepository<AppCandidate, Long> {
    List<AppCandidate> findByIsSyntheticTrue();
    void deleteByIsSyntheticTrue();
}
