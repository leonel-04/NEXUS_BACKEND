
package com.nexuserp.backend.ventascomprobantes.controller;

import com.nexuserp.backend.ventascomprobantes.model.Comprobante;
import com.nexuserp.backend.ventascomprobantes.service.ComprobanteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comprobantes")
@CrossOrigin(origins = "http://localhost:4200")
public class ComprobanteController {

    private final ComprobanteService comprobanteService;

    public ComprobanteController(ComprobanteService comprobanteService) {
        this.comprobanteService = comprobanteService;
    }

    @GetMapping
    public ResponseEntity<List<Comprobante>> listarComprobantes() {
        return ResponseEntity.ok(comprobanteService.listarComprobantes());
    }
}
