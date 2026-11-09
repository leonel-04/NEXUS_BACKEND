
package com.nexuserp.backend.ventascomprobantes.service;

import com.nexuserp.backend.inventarioalmacen.model.Inventario;
import com.nexuserp.backend.inventarioalmacen.service.InventarioService;
import com.nexuserp.backend.ventascomprobantes.dto.DetalleVentaDTO;
import com.nexuserp.backend.ventascomprobantes.dto.VentaDTO;
import com.nexuserp.backend.ventascomprobantes.model.Comprobante;
import com.nexuserp.backend.ventascomprobantes.model.DetalleVenta;
import com.nexuserp.backend.ventascomprobantes.model.Venta;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class VentaService {

    private final List<Venta> ventas = new ArrayList<>();

    private final InventarioService inventarioService;
    private final ComprobanteService comprobanteService;

    private Long siguienteId = 1L;

    public VentaService(InventarioService inventarioService,
                        ComprobanteService comprobanteService) {
        this.inventarioService = inventarioService;
        this.comprobanteService = comprobanteService;
    }

    public List<Venta> listarVentas() {
        return ventas;
    }

    public Venta buscarVenta(Long id) {
        return ventas.stream()
                .filter(venta -> venta.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Venta registrarVenta(VentaDTO datos) {

        if (datos.getCliente() == null || datos.getCliente().isBlank()) {
            throw new IllegalArgumentException("Debe indicar el cliente.");
        }

        if (datos.getNumeroDocumento() == null ||
                datos.getNumeroDocumento().isBlank()) {
            throw new IllegalArgumentException("Debe indicar el número de documento del cliente.");
        }

        if (datos.getTipoComprobante() == null ||
                (!datos.getTipoComprobante().equalsIgnoreCase("BOLETA")
                        && !datos.getTipoComprobante().equalsIgnoreCase("FACTURA"))) {
            throw new IllegalArgumentException("El comprobante debe ser BOLETA o FACTURA.");
        }

        if (datos.getDetalles() == null || datos.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener productos.");
        }

        List<DetalleVenta> detalles = new ArrayList<>();
        Set<String> codigosRegistrados = new HashSet<>();

        // Primero validamos todos los productos y el stock.
        for (DetalleVentaDTO detalleDTO : datos.getDetalles()) {

            if (detalleDTO.getCodigoProducto() == null ||
                    detalleDTO.getCodigoProducto().isBlank()) {
                throw new IllegalArgumentException("Debe indicar el código del producto.");
            }

            if (!codigosRegistrados.add(detalleDTO.getCodigoProducto())) {
                throw new IllegalArgumentException("No repita el mismo producto en una venta.");
            }

            if (detalleDTO.getCantidad() <= 0 ||
                    detalleDTO.getPrecioUnitario() <= 0) {
                throw new IllegalArgumentException("La cantidad y el precio deben ser mayores a cero.");
            }

            Inventario producto = inventarioService.listarInventario().stream()
                    .filter(item -> item.getCodigoProducto()
                            .equals(detalleDTO.getCodigoProducto()))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalArgumentException("El producto no existe en el inventario.")
                    );

            if (producto.getStock() < detalleDTO.getCantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto: " + producto.getNombreProducto()
                );
            }

            detalles.add(new DetalleVenta(
                    (long) (detalles.size() + 1),
                    producto.getCodigoProducto(),
                    producto.getNombreProducto(),
                    detalleDTO.getCantidad(),
                    detalleDTO.getPrecioUnitario()
            ));
        }

        Venta venta = new Venta(
                siguienteId,
                "VENT-" + String.format("%04d", siguienteId),
                datos.getCliente(),
                LocalDateTime.now(),
                detalles,
                "REGISTRADA"
        );

        // Descontamos del inventario después de validar todos los productos.
        for (DetalleVenta detalle : detalles) {
            inventarioService.registrarMovimiento(
                    detalle.getCodigoProducto(),
                    "SALIDA",
                    detalle.getCantidad(),
                    "Venta " + venta.getNumeroVenta()
            );
        }

        ventas.add(venta);

        // Generamos el comprobante de la venta.
        Comprobante comprobante = comprobanteService.registrarComprobante(
                venta,
                datos.getTipoComprobante(),
                datos.getNumeroDocumento()
        );

        siguienteId++;

        return venta;
    }
}
