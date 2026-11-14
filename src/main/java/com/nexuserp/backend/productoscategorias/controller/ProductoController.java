
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

    // Listar productos
    @GetMapping
    public List<ProductoDTO> listarProductos() {
        return productoService.listarProductos();
    }

    // Buscar producto por ID
    @GetMapping("/{id}")
    public ProductoDTO buscarProducto(@PathVariable int id) {
        return productoService.buscarProducto(id);
    }

    // Registrar producto
    @PostMapping
    public ProductoDTO registrarProducto(@RequestBody ProductoDTO producto) {
        return productoService.registrarProducto(producto);
    }

    // Modificar producto
    @PutMapping("/{id}")
    public ProductoDTO modificarProducto(
            @PathVariable int id,
            @RequestBody ProductoDTO producto) {

        return productoService.modificarProducto(id, producto);
    }

    // Eliminar producto (desactivacion)
    @DeleteMapping("/{id}")
    public String eliminarProducto(@PathVariable int id) {

        if (productoService.eliminarProducto(id)) {
            return "Producto desactivado correctamente";
        }

        return "Producto no encontrado";
    }
}
