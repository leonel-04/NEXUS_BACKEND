
package com.nexuserp.backend.usuariosroles.controller;

import com.nexuserp.backend.usuariosroles.dto.UsuarioDTO;
import com.nexuserp.backend.usuariosroles.model.Usuario;
import com.nexuserp.backend.usuariosroles.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.buscarUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioDTO dto) {
        try {
            Usuario usuario = usuarioService.registrarUsuario(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody UsuarioDTO dto) {
        try {
            Usuario usuario = usuarioService.actualizarUsuario(id, dto);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> desactivarUsuario(@PathVariable Long id) {
        try {
            usuarioService.desactivarUsuario(id);
            return ResponseEntity.ok(
                    "Usuario desactivado correctamente."
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
