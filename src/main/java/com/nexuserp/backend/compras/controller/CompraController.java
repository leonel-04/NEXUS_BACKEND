
package com.nexuserp.backend.compras.controller;

import com.nexuserp.backend.compras.dto.CompraDTO;
import com.nexuserp.backend.compras.model.Compra;
import com.nexuserp.backend.compras.service.CompraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
@CrossOrigin(origins = "http://localhost:4200")
public class CompraController {

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping
    public ResponseEntity<List<Compra>> listarCompras() {
        return ResponseEntity.ok(compraService.listarCompras());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCompra(@PathVariable Long id) {

        Compra compra = compraService.buscarCompra(id);

        if (compra == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Compra no encontrada.");
        }

        return ResponseEntity.ok(compra);
    }

    @PostMapping
    public ResponseEntity<?> registrarCompra(@RequestBody CompraDTO datos) {

        try {
            Compra compra = compraService.registrarCompra(datos);

            return ResponseEntity.status(HttpStatus.CREATED).body(compra);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
