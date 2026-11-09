
package com.nexuserp.backend.productoscategorias.controller;

import com.nexuserp.backend.productoscategorias.dto.ProductoDTO;
import com.nexuserp.backend.productoscategorias.service.ProductoService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoDTO> listarProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("/{id}")
    public ProductoDTO buscarProducto(@PathVariable int id) {
        return productoService.buscarProducto(id);
    }

    @PostMapping
    public ProductoDTO registrarProducto(@RequestBody ProductoDTO producto) {
        return productoService.registrarProducto(producto);
    }

    @DeleteMapping("/{id}")
    public String eliminarProducto(@PathVariable int id) {

        if (productoService.eliminarProducto(id)) {
            return "Producto desactivado correctamente";
        }

        return "Producto no encontrado";
    }
}
