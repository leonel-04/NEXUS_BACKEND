
package com.nexuserp.backend.clientesproveedores.controller;

import com.nexuserp.backend.clientesproveedores.dto.ProveedorDTO;
import com.nexuserp.backend.clientesproveedores.model.Proveedor;
import com.nexuserp.backend.clientesproveedores.service.ProveedorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "http://localhost:4200")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public ResponseEntity<List<Proveedor>> listarProveedores() {
        return ResponseEntity.ok(proveedorService.listarProveedores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarProveedor(@PathVariable Long id) {

        Proveedor proveedor = proveedorService.buscarProveedor(id);

        if (proveedor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Proveedor no encontrado.");
        }

        return ResponseEntity.ok(proveedor);
    }

    @PostMapping
    public ResponseEntity<?> registrarProveedor(@RequestBody ProveedorDTO datos) {

        try {
            Proveedor proveedor = proveedorService.registrarProveedor(datos);

            return ResponseEntity.status(HttpStatus.CREATED).body(proveedor);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProveedor(
            @PathVariable Long id,
            @RequestBody ProveedorDTO datos) {

        try {
            Proveedor proveedor = proveedorService.actualizarProveedor(id, datos);

            if (proveedor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Proveedor no encontrado.");
            }

            return ResponseEntity.ok(proveedor);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> desactivarProveedor(@PathVariable Long id) {

        boolean desactivado = proveedorService.desactivarProveedor(id);

        if (!desactivado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Proveedor no encontrado.");
        }

        return ResponseEntity.ok("Proveedor desactivado correctamente.");
    }
}
