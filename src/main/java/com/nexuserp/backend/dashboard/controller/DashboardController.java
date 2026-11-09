
package com.nexuserp.backend.dashboard.controller;

import com.nexuserp.backend.dashboard.dto.DashboardResumenDTO;
import com.nexuserp.backend.dashboard.service.DashboardService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/resumen")
    public DashboardResumenDTO obtenerResumen() {
        return dashboardService.obtenerResumen();
    }
}
