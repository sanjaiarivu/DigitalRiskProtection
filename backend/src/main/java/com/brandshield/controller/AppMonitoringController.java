package com.brandshield.controller;

import com.brandshield.dto.response.AppCandidateResponse;
import com.brandshield.dto.response.ScanResultResponse;
import com.brandshield.dto.request.AppCandidateCreateRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import com.brandshield.service.AppMonitoringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/apps")
@CrossOrigin(origins = "*")
public class AppMonitoringController {

    private final AppMonitoringService appMonitoringService;

    public AppMonitoringController(AppMonitoringService appMonitoringService) {
        this.appMonitoringService = appMonitoringService;
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<AppCandidateResponse>> getCandidates() {
        return ResponseEntity.ok(appMonitoringService.getCandidates());
    }

    @PostMapping("/candidates")
    public ResponseEntity<AppCandidateResponse> addCandidate(@Valid @RequestBody AppCandidateCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appMonitoringService.addCandidate(request));
    }

    @PostMapping("/scan")
    public ResponseEntity<ScanResultResponse> scanAllBrands() {
        return ResponseEntity.ok(appMonitoringService.scanAllBrands());
    }
}
