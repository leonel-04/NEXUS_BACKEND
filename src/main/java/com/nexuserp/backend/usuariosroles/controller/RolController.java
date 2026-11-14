
package com.nexuserp.backend.usuariosroles.controller;

import com.nexuserp.backend.usuariosroles.dto.RolDTO;
import com.nexuserp.backend.usuariosroles.model.Rol;
import com.nexuserp.backend.usuariosroles.service.RolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:4200")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public ResponseEntity<List<Rol>> listarRoles() {
        return ResponseEntity.ok(rolService.listarRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarRol(@PathVariable Long id) {
        try {
            Rol rol = rolService.buscarRol(id);
            return ResponseEntity.ok(rol);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registrarRol(@RequestBody RolDTO dto) {
        try {
            Rol rol = rolService.registrarRol(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(rol);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarRol(
            @PathVariable Long id,
            @RequestBody RolDTO dto) {
        try {
            Rol rol = rolService.actualizarRol(id, dto);
            return ResponseEntity.ok(rol);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> desactivarRol(@PathVariable Long id) {
        try {
            rolService.desactivarRol(id);
            return ResponseEntity.ok("Rol desactivado correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
