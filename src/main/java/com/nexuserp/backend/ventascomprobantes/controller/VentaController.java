
package com.nexuserp.backend.ventascomprobantes.controller;

import com.nexuserp.backend.ventascomprobantes.dto.VentaDTO;
import com.nexuserp.backend.ventascomprobantes.model.Venta;
import com.nexuserp.backend.ventascomprobantes.service.VentaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "http://localhost:4200")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.listarVentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarVenta(@PathVariable Long id) {

        Venta venta = ventaService.buscarVenta(id);

        if (venta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Venta no encontrada.");
        }

        return ResponseEntity.ok(venta);
    }

    @PostMapping
    public ResponseEntity<?> registrarVenta(@RequestBody VentaDTO datos) {

        try {
            Venta venta = ventaService.registrarVenta(datos);

            return ResponseEntity.status(HttpStatus.CREATED).body(venta);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/anular")
    public ResponseEntity<?> anularVenta(@PathVariable Long id) {

        try {
            boolean anulada = ventaService.anularVenta(id);

            if (!anulada) {
                return ResponseEntity.badRequest()
                        .body("No se pudo anular la venta.");
            }

            return ResponseEntity.ok(
                    "La venta ha sido anulada correctamente."
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
