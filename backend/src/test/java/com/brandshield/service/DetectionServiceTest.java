package com.brandshield.service;

import com.brandshield.dto.request.NoteCreateRequest;
import com.brandshield.dto.request.StatusUpdateRequest;
import com.brandshield.dto.response.InvestigationNoteResponse;
import com.brandshield.dto.response.ThreatDetectionResponse;
import com.brandshield.entity.Brand;
import com.brandshield.entity.ThreatDetection;
import com.brandshield.exception.ValidationException;
import com.brandshield.repository.ThreatDetectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetectionServiceTest {

    @Mock
    private ThreatDetectionRepository detectionRepository;

    @InjectMocks
    private DetectionService detectionService;

    private ThreatDetection sampleDetection;

    @BeforeEach
    void setUp() {
        Brand brand = Brand.builder().id(1L).name("TechVault").build();
        sampleDetection = ThreatDetection.builder()
                .id(100L)
                .brand(brand)
                .detectionType(ThreatDetection.DetectionType.BRAND_IMPERSONATION)
                .candidateType(ThreatDetection.CandidateType.SOCIAL)
                .riskScore(85)
                .riskLevel(ThreatDetection.RiskLevel.CRITICAL)
                .status(ThreatDetection.DetectionStatus.NEW)
                .isOfficialAsset(false)
                .summary("Look-alike username @techvau1t")
                .detectedAt(LocalDateTime.now())
                .history(new ArrayList<>())
                .notes(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Status update transition from NEW to UNDER_INVESTIGATION should record history")
    void testUpdateStatusTransition() {
        when(detectionRepository.findById(100L)).thenReturn(Optional.of(sampleDetection));
        when(detectionRepository.save(any(ThreatDetection.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StatusUpdateRequest request = StatusUpdateRequest.builder()
                .status("UNDER_INVESTIGATION")
                .reason("Investigating spoofed profile")
                .build();

        ThreatDetectionResponse response = detectionService.updateStatus(100L, request);

        assertEquals("UNDER_INVESTIGATION", response.getStatus());
        assertEquals(1, sampleDetection.getHistory().size());
        assertEquals("NEW", sampleDetection.getHistory().get(0).getPreviousStatus());
        assertEquals("UNDER_INVESTIGATION", sampleDetection.getHistory().get(0).getNewStatus());
        assertEquals("Investigating spoofed profile", sampleDetection.getHistory().get(0).getChangeReason());
    }

    @Test
    @DisplayName("Invalid status string should throw ValidationException")
    void testInvalidStatusThrowsValidationException() {
        when(detectionRepository.findById(100L)).thenReturn(Optional.of(sampleDetection));

        StatusUpdateRequest request = StatusUpdateRequest.builder()
                .status("INVALID_STATUS_STRING")
                .build();

        assertThrows(ValidationException.class, () -> detectionService.updateStatus(100L, request));
    }

    @Test
    @DisplayName("Adding investigation note should append note to detection entity")
    void testAddNote() {
        when(detectionRepository.findById(100L)).thenReturn(Optional.of(sampleDetection));
        when(detectionRepository.save(any(ThreatDetection.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NoteCreateRequest request = NoteCreateRequest.builder()
                .noteText("Confirmed phishing link detected in bio")
                .author("SecAnalyst1")
                .build();

        InvestigationNoteResponse response = detectionService.addNote(100L, request);

        assertEquals("SecAnalyst1", response.getAuthor());
        assertEquals("Confirmed phishing link detected in bio", response.getNoteText());
        assertEquals(1, sampleDetection.getNotes().size());
    }
}
