
package com.nexuserp.backend.reportes.controller;

import com.nexuserp.backend.reportes.dto.ReporteComprasDTO;
import com.nexuserp.backend.reportes.dto.ReporteInventarioDTO;
import com.nexuserp.backend.reportes.dto.ReporteVentasDTO;
import com.nexuserp.backend.reportes.service.ReporteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "http://localhost:4200")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/ventas")
    public ReporteVentasDTO reporteVentas(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaInicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaFin) {

        return reporteService.generarReporteVentas(
                fechaInicio,
                fechaFin
        );
    }

    @GetMapping("/compras")
    public ReporteComprasDTO reporteCompras(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaInicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaFin) {

        return reporteService.generarReporteCompras(
                fechaInicio,
                fechaFin
        );
    }

    @GetMapping("/inventario")
    public ReporteInventarioDTO reporteInventario() {

        return reporteService.generarReporteInventario();
    }
}
