package com.brandshield.controller;

import com.brandshield.dto.request.NoteCreateRequest;
import com.brandshield.dto.request.StatusUpdateRequest;
import com.brandshield.dto.response.AiExplanationResponse;
import com.brandshield.dto.response.InvestigationNoteResponse;
import com.brandshield.dto.response.ThreatDetectionDetailResponse;
import com.brandshield.dto.response.ThreatDetectionResponse;
import com.brandshield.entity.ThreatDetection;
import com.brandshield.exception.ResourceNotFoundException;
import com.brandshield.repository.ThreatDetectionRepository;
import com.brandshield.service.AiExplanationOrchestrator;
import com.brandshield.service.DetectionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/detections")
@CrossOrigin(origins = "*")
public class DetectionController {

    private final DetectionService detectionService;
    private final ThreatDetectionRepository detectionRepository;
    private final AiExplanationOrchestrator explanationOrchestrator;

    public DetectionController(
            DetectionService detectionService,
            ThreatDetectionRepository detectionRepository,
            AiExplanationOrchestrator explanationOrchestrator) {
        this.detectionService = detectionService;
        this.detectionRepository = detectionRepository;
        this.explanationOrchestrator = explanationOrchestrator;
    }

    @GetMapping
    public ResponseEntity<List<ThreatDetectionResponse>> getDetections(
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String riskLevel,
            @RequestParam(required = false) String candidateType) {
        List<ThreatDetectionResponse> list = detectionService.getDetections(brandId, status, riskLevel, candidateType);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThreatDetectionDetailResponse> getDetection(@PathVariable Long id) {
        return ResponseEntity.ok(detectionService.getDetection(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ThreatDetectionResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(detectionService.updateStatus(id, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ThreatDetectionResponse> updateStatusPut(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(detectionService.updateStatus(id, request));
    }

    @PostMapping("/{id}/notes")
    public ResponseEntity<InvestigationNoteResponse> addNote(
            @PathVariable Long id,
            @Valid @RequestBody NoteCreateRequest request) {
        return ResponseEntity.ok(detectionService.addNote(id, request));
    }

    @PostMapping(value = {"/{id}/explanation", "/{id}/ai-explanation"})
    @Transactional
    public ResponseEntity<AiExplanationResponse> generateExplanation(@PathVariable Long id) {
        ThreatDetection detection = detectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detection", id));

        AiExplanationResponse explanation = explanationOrchestrator.generateExplanation(
                detection, detection.getEvidence());

        if (explanation != null && explanation.getSummary() != null) {
            detection.setAiExplanation(explanation.getSummary());
            detectionRepository.save(detection);
        }

        return ResponseEntity.ok(explanation);
    }

    @GetMapping(value = {"/{id}/explanation", "/{id}/ai-explanation"})
    @Transactional(readOnly = true)
    public ResponseEntity<AiExplanationResponse> getExplanation(@PathVariable Long id) {
        ThreatDetection detection = detectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detection", id));

        AiExplanationResponse explanation = explanationOrchestrator.generateExplanation(
                detection, detection.getEvidence());

        return ResponseEntity.ok(explanation);
    }
}
