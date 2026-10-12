package com.brandshield.controller;

import com.brandshield.service.DemoDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/demo")
@CrossOrigin(origins = "*")
public class DemoController {

    private final DemoDataService demoDataService;

    public DemoController(DemoDataService demoDataService) {
        this.demoDataService = demoDataService;
    }

    @PostMapping("/seed")
    public ResponseEntity<Map<String, String>> seedDemoData() {
        String message = demoDataService.seedDemoData();
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/clear")
    public ResponseEntity<Map<String, String>> clearDemoData() {
        String message = demoDataService.clearDemoData();
        return ResponseEntity.ok(Map.of("message", message));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, String>> clearDemoDataDelete() {
        String message = demoDataService.clearDemoData();
        return ResponseEntity.ok(Map.of("message", message));
    }
}
