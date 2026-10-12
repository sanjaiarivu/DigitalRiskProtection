package com.brandshield.controller;

import com.brandshield.dto.response.ScanResultResponse;
import com.brandshield.dto.response.SocialCandidateResponse;
import com.brandshield.service.SocialMonitoringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/social")
@CrossOrigin(origins = "*")
public class SocialMonitoringController {

    private final SocialMonitoringService socialMonitoringService;

    public SocialMonitoringController(SocialMonitoringService socialMonitoringService) {
        this.socialMonitoringService = socialMonitoringService;
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<SocialCandidateResponse>> getCandidates() {
        return ResponseEntity.ok(socialMonitoringService.getCandidates());
    }

    @PostMapping("/scan")
    public ResponseEntity<ScanResultResponse> scanAllBrands() {
        return ResponseEntity.ok(socialMonitoringService.scanAllBrands());
    }
}
