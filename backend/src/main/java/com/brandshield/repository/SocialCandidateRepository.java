package com.brandshield.repository;

import com.brandshield.entity.SocialCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SocialCandidateRepository extends JpaRepository<SocialCandidate, Long> {
    List<SocialCandidate> findByIsSyntheticTrue();
    void deleteByIsSyntheticTrue();
}
