package com.brandshield.service;

import com.brandshield.dto.request.NoteCreateRequest;
import com.brandshield.dto.request.StatusUpdateRequest;
import com.brandshield.dto.response.InvestigationNoteResponse;
import com.brandshield.dto.response.ThreatDetectionDetailResponse;
import com.brandshield.dto.response.ThreatDetectionResponse;
import com.brandshield.entity.InvestigationHistory;
import com.brandshield.entity.InvestigationNote;
import com.brandshield.entity.ThreatDetection;
import com.brandshield.exception.ResourceNotFoundException;
import com.brandshield.exception.ValidationException;
import com.brandshield.mapper.EntityMapper;
import com.brandshield.repository.ThreatDetectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetectionService {

    private final ThreatDetectionRepository detectionRepository;

    public DetectionService(ThreatDetectionRepository detectionRepository) {
        this.detectionRepository = detectionRepository;
    }

    @Transactional(readOnly = true)
    public List<ThreatDetectionResponse> getDetections(
            Long brandId, String status, String riskLevel, String candidateType) {

        ThreatDetection.DetectionStatus statusEnum = null;
        ThreatDetection.RiskLevel riskLevelEnum = null;
        ThreatDetection.CandidateType candidateTypeEnum = null;

        try {
            if (status != null && !status.isBlank()) {
                statusEnum = ThreatDetection.DetectionStatus.valueOf(status.toUpperCase());
            }
            if (riskLevel != null && !riskLevel.isBlank()) {
                riskLevelEnum = ThreatDetection.RiskLevel.valueOf(riskLevel.toUpperCase());
            }
            if (candidateType != null && !candidateType.isBlank()) {
                candidateTypeEnum = ThreatDetection.CandidateType.valueOf(candidateType.toUpperCase());
            }
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid filter value: " + e.getMessage());
        }

        return detectionRepository.findFiltered(brandId, statusEnum, riskLevelEnum, candidateTypeEnum)
                .stream()
                .map(EntityMapper::toThreatDetectionResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ThreatDetectionDetailResponse getDetection(Long id) {
        ThreatDetection detection = detectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detection", id));
        return EntityMapper.toThreatDetectionDetailResponse(detection);
    }

    @Transactional
    public ThreatDetectionResponse updateStatus(Long id, StatusUpdateRequest request) {
        ThreatDetection detection = detectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detection", id));

        ThreatDetection.DetectionStatus newStatus;
        try {
            newStatus = ThreatDetection.DetectionStatus.valueOf(request.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid status: " + request.getStatus());
        }

        String previousStatus = detection.getStatus().name();

        // Record history
        InvestigationHistory history = InvestigationHistory.builder()
                .detection(detection)
                .previousStatus(previousStatus)
                .newStatus(newStatus.name())
                .changeReason(request.getReason())
                .changedBy("analyst")
                .build();
        detection.getHistory().add(history);

        detection.setStatus(newStatus);
        detection = detectionRepository.save(detection);

        return EntityMapper.toThreatDetectionResponse(detection);
    }

    @Transactional
    public InvestigationNoteResponse addNote(Long id, NoteCreateRequest request) {
        ThreatDetection detection = detectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detection", id));

        InvestigationNote note = InvestigationNote.builder()
                .detection(detection)
                .noteText(request.getNoteText())
                .author(request.getAuthor() != null ? request.getAuthor() : "analyst")
                .build();

        detection.getNotes().add(note);
        detectionRepository.save(detection);

        return EntityMapper.toNoteResponse(note);
    }
}
