
package com.nexuserp.backend.clientesproveedores.controller;

import com.nexuserp.backend.clientesproveedores.dto.ClienteDTO;
import com.nexuserp.backend.clientesproveedores.model.Cliente;
import com.nexuserp.backend.clientesproveedores.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarCliente(@PathVariable Long id) {

        Cliente cliente = clienteService.buscarCliente(id);

        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente no encontrado.");
        }

        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    public ResponseEntity<?> registrarCliente(@RequestBody ClienteDTO datos) {

        try {
            Cliente cliente = clienteService.registrarCliente(datos);

            return ResponseEntity.status(HttpStatus.CREATED).body(cliente);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(
            @PathVariable Long id,
            @RequestBody ClienteDTO datos) {

        try {
            Cliente cliente = clienteService.actualizarCliente(id, datos);

            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Cliente no encontrado.");
            }

            return ResponseEntity.ok(cliente);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> desactivarCliente(@PathVariable Long id) {

        boolean desactivado = clienteService.desactivarCliente(id);

        if (!desactivado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cliente no encontrado.");
        }

        return ResponseEntity.ok("Cliente desactivado correctamente.");
    }
}
