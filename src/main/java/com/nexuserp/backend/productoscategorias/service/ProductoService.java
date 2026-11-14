
package com.nexuserp.backend.productoscategorias.service;

import com.nexuserp.backend.productoscategorias.dto.ProductoDTO;
import com.nexuserp.backend.productoscategorias.model.Producto;
import com.nexuserp.backend.productoscategorias.repository.ProductoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Registrar productos iniciales si la tabla está vacía
    @PostConstruct
    public void cargarProductosIniciales() {

        if (productoRepository.count() == 0) {

            registrarProductoInicial(
                    "Teclado",
                    "Teclado USB",
                    "Perifericos",
                    45.00,
                    30,
                    5
            );

            registrarProductoInicial(
                    "Mouse",
                    "Mouse optico",
                    "Perifericos",
                    25.00,
                    50,
                    10
            );

            registrarProductoInicial(
                    "Monitor",
                    "Monitor LED 24 pulgadas",
                    "Monitores",
                    650.00,
                    4,
                    5
            );
        }
    }

    // Guardar productos iniciales
    private void registrarProductoInicial(
            String nombre,
            String descripcion,
            String categoria,
            double precio,
            Integer stock,
            Integer stockMinimo) {

        Producto producto = new Producto();

        producto.setCodigo("TMP-" + UUID.randomUUID().toString().substring(0, 12));        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setCategoria(categoria);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setStockMinimo(stockMinimo);
        producto.setActivo(true);

        producto = productoRepository.save(producto);

        producto.setCodigo(
                String.format("PROD-%03d", producto.getId())
        );

        productoRepository.save(producto);
    }

    // Listar productos
    public List<ProductoDTO> listarProductos() {

        return productoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    // Buscar producto por ID
    public ProductoDTO buscarProducto(int id) {

        return productoRepository.findById(id)
                .map(this::convertirADTO)
                .orElse(null);
    }

    // Registrar producto
    public ProductoDTO registrarProducto(ProductoDTO datos) {

        Producto producto = new Producto();

        producto.setCodigo("TMP-" + UUID.randomUUID().toString().substring(0, 12));
        producto.setNombre(datos.getNombre());
        producto.setDescripcion(datos.getDescripcion());
        producto.setCategoria(datos.getCategoria());
        producto.setPrecio(datos.getPrecio());
        producto.setStock(datos.getStock());
        producto.setStockMinimo(datos.getStockMinimo());
        producto.setActivo(true);

        producto = productoRepository.save(producto);

        producto.setCodigo(
                String.format("PROD-%03d", producto.getId())
        );

        producto = productoRepository.save(producto);

        return convertirADTO(producto);
    }

    // Modificar producto
    public ProductoDTO modificarProducto(int id, ProductoDTO datos) {

        Producto producto = productoRepository.findById(id)
                .orElse(null);

        if (producto == null || !Boolean.TRUE.equals(producto.getActivo())) {
            return null;
        }

        producto.setNombre(datos.getNombre());
        producto.setDescripcion(datos.getDescripcion());
        producto.setCategoria(datos.getCategoria());
        producto.setPrecio(datos.getPrecio());
        producto.setStock(datos.getStock());
        producto.setStockMinimo(datos.getStockMinimo());

        producto = productoRepository.save(producto);

        return convertirADTO(producto);
    }

    // Eliminar producto (desactivación lógica)
    public boolean eliminarProducto(int id) {

        Producto producto = productoRepository.findById(id)
                .orElse(null);

        if (producto == null) {
            return false;
        }

        producto.setActivo(false);

        productoRepository.save(producto);

        return true;
    }

    // Convertir entidad a DTO
    private ProductoDTO convertirADTO(Producto producto) {

        return new ProductoDTO(
                producto.getId(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getCategoria(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getStockMinimo(),
                producto.getActivo()
        );
    }
}
