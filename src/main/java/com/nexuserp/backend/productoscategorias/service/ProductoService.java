
package com.nexuserp.backend.productoscategorias.service;

import com.nexuserp.backend.productoscategorias.dto.ProductoDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoService {

    private final List<ProductoDTO> productos = new ArrayList<>();

    private int siguienteId = 4;

    public ProductoService() {

        productos.add(new ProductoDTO(1, "PROD-001",
                "Teclado", "Teclado USB", "Perifericos",
                45.00, 30, 5, true));

        productos.add(new ProductoDTO(2, "PROD-002",
                "Mouse", "Mouse optico", "Perifericos",
                25.00, 50, 10, true));

        productos.add(new ProductoDTO(3, "PROD-003",
                "Monitor", "Monitor LED 24 pulgadas", "Monitores",
                650.00, 4, 5, true));
    }

    public List<ProductoDTO> listarProductos() {
        return productos;
    }

    public ProductoDTO buscarProducto(int id) {
        return productos.stream()
                .filter(producto -> producto.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public ProductoDTO registrarProducto(ProductoDTO producto) {

        producto = new ProductoDTO(
                siguienteId,
                "PROD-" + String.format("%03d", siguienteId),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getCategoria(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getStockMinimo(),
                true
        );

        siguienteId++;
        productos.add(producto);

        return producto;
    }

    public boolean eliminarProducto(int id) {

        ProductoDTO producto = buscarProducto(id);

        if (producto == null) {
            return false;
        }

        producto.setActivo(false);
        return true;
    }
}
