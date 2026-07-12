package com.sentimentanalysis.controller;

import com.sentimentanalysis.dto.DashboardStats;
import com.sentimentanalysis.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStats> dashboard() {
        return ResponseEntity.ok(dashboardService.loadDashboardStats());
    }

    @GetMapping("/statistics")
    public ResponseEntity<DashboardStats> statistics() {
        return ResponseEntity.ok(dashboardService.loadDashboardStats());
    }
}
