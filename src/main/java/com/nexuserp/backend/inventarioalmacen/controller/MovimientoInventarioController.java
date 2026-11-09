
package com.nexuserp.backend.inventarioalmacen.controller;

import com.nexuserp.backend.inventarioalmacen.dto.MovimientoInventarioDTO;
import com.nexuserp.backend.inventarioalmacen.model.MovimientoInventario;
import com.nexuserp.backend.inventarioalmacen.service.InventarioService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario/movimientos")
@CrossOrigin(origins = "http://localhost:4200")
public class MovimientoInventarioController {

    private final InventarioService inventarioService;

    public MovimientoInventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public List<MovimientoInventario> listarMovimientos() {
        return inventarioService.listarMovimientos();
    }

    @PostMapping
    public String registrarMovimiento(@RequestBody MovimientoInventarioDTO dto) {

        if (inventarioService.registrarMovimiento(dto)) {
            return "Movimiento registrado correctamente";
        }

        return "No se pudo registrar el movimiento. Verifique los datos y el stock.";
    }
}
