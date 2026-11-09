
package com.nexuserp.backend.inventarioalmacen.controller;

import com.nexuserp.backend.inventarioalmacen.dto.InventarioDTO;
import com.nexuserp.backend.inventarioalmacen.service.InventarioService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "http://localhost:4200")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public List<InventarioDTO> listarInventario() {
        return inventarioService.listarInventario();
    }

    @GetMapping("/stock-bajo")
    public List<InventarioDTO> listarStockBajo() {
        return inventarioService.listarStockBajo();
    }
}
