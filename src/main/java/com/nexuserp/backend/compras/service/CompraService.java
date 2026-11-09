
package com.nexuserp.backend.compras.service;

import com.nexuserp.backend.compras.dto.CompraDTO;
import com.nexuserp.backend.compras.dto.DetalleCompraDTO;
import com.nexuserp.backend.compras.model.Compra;
import com.nexuserp.backend.compras.model.DetalleCompra;
import com.nexuserp.backend.inventarioalmacen.service.InventarioService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompraService {

    private final List<Compra> compras = new ArrayList<>();
    private final InventarioService inventarioService;

    private Long siguienteId = 1L;

    public CompraService(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    public List<Compra> listarCompras() {
        return compras;
    }

    public Compra buscarCompra(Long id) {
        return compras.stream()
                .filter(compra -> compra.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Compra registrarCompra(CompraDTO datos) {

        if (datos.getProveedor() == null || datos.getProveedor().isBlank()) {
            throw new IllegalArgumentException("Debe indicar el proveedor.");
        }

        if (datos.getDetalles() == null || datos.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La compra debe tener productos.");
        }

        List<DetalleCompra> detalles = new ArrayList<>();

        for (DetalleCompraDTO detalleDTO : datos.getDetalles()) {

            if (detalleDTO.getCodigoProducto() == null ||
                    detalleDTO.getCodigoProducto().isBlank()) {
                throw new IllegalArgumentException("Debe indicar el código del producto.");
            }

            if (detalleDTO.getCantidad() <= 0 ||
                    detalleDTO.getPrecioUnitario() <= 0) {
                throw new IllegalArgumentException("La cantidad y el precio deben ser mayores a cero.");
            }

            DetalleCompra detalle = new DetalleCompra(
                    (long) (detalles.size() + 1),
                    detalleDTO.getCodigoProducto(),
                    obtenerNombreProducto(detalleDTO.getCodigoProducto()),
                    detalleDTO.getCantidad(),
                    detalleDTO.getPrecioUnitario()
            );

            detalles.add(detalle);
        }

        Compra compra = new Compra(
                siguienteId,
                "COMP-" + String.format("%04d", siguienteId),
                datos.getProveedor(),
                LocalDateTime.now(),
                detalles,
                "REGISTRADA"
        );

        // Registrar las entradas de productos en el inventario.
        for (DetalleCompra detalle : detalles) {
            inventarioService.registrarMovimiento(
                    detalle.getCodigoProducto(),
                    "ENTRADA",
                    detalle.getCantidad(),
                    "Compra " + compra.getNumeroCompra()
            );
        }

        compras.add(compra);
        siguienteId++;

        return compra;
    }

    private String obtenerNombreProducto(String codigoProducto) {

        return inventarioService.listarInventario().stream()
                .filter(producto -> producto.getCodigoProducto().equals(codigoProducto))
                .map(producto -> producto.getNombreProducto())
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("El producto no existe en el inventario.")
                );
    }
}
