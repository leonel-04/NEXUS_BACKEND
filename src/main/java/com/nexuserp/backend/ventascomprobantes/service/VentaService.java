
package com.nexuserp.backend.ventascomprobantes.service;

import com.nexuserp.backend.inventarioalmacen.dto.InventarioDTO;
import com.nexuserp.backend.inventarioalmacen.dto.MovimientoInventarioDTO;
import com.nexuserp.backend.inventarioalmacen.service.InventarioService;
import com.nexuserp.backend.ventascomprobantes.dto.DetalleVentaDTO;
import com.nexuserp.backend.ventascomprobantes.dto.VentaDTO;
import com.nexuserp.backend.ventascomprobantes.model.Comprobante;
import com.nexuserp.backend.ventascomprobantes.model.DetalleVenta;
import com.nexuserp.backend.ventascomprobantes.model.Venta;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class VentaService {

    private final List<Venta> ventas = new ArrayList<>();

    private final Map<String, Venta> solicitudesProcesadas = new HashMap<>();

    private final InventarioService inventarioService;
    private final ComprobanteService comprobanteService;

    private Long siguienteId = 1L;

    public VentaService(
            InventarioService inventarioService,
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

    public synchronized Venta registrarVenta(VentaDTO datos) {

        if (datos.getIdSolicitud() == null ||
                datos.getIdSolicitud().isBlank()) {
            throw new IllegalArgumentException(
                    "La solicitud de venta debe tener un identificador.");
        }

        String idSolicitud = datos.getIdSolicitud().trim();

        // Comprobamos si esta solicitud ya fue procesada.
        Venta ventaExistente = solicitudesProcesadas.get(idSolicitud);

        if (ventaExistente != null) {
            return ventaExistente;
        }

        if (datos.getCliente() == null || datos.getCliente().isBlank()) {
            throw new IllegalArgumentException("Debe indicar el cliente.");
        }

        if (datos.getNumeroDocumento() == null ||
                datos.getNumeroDocumento().isBlank()) {
            throw new IllegalArgumentException(
                    "Debe indicar el número de documento del cliente.");
        }

        if (datos.getTipoComprobante() == null ||
                (!datos.getTipoComprobante().equalsIgnoreCase("BOLETA")
                        && !datos.getTipoComprobante().equalsIgnoreCase("FACTURA"))) {
            throw new IllegalArgumentException(
                    "El comprobante debe ser BOLETA o FACTURA.");
        }

        if (datos.getDetalles() == null || datos.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener productos.");
        }

        List<DetalleVenta> detalles = new ArrayList<>();
        Set<String> codigosRegistrados = new HashSet<>();

        // Validamos todos los productos antes de descontar el stock.
        for (DetalleVentaDTO detalleDTO : datos.getDetalles()) {

            if (detalleDTO.getCodigoProducto() == null ||
                    detalleDTO.getCodigoProducto().isBlank()) {
                throw new IllegalArgumentException(
                        "Debe indicar el código del producto.");
            }

            if (!codigosRegistrados.add(
                    detalleDTO.getCodigoProducto().toUpperCase())) {
                throw new IllegalArgumentException(
                        "No repita el mismo producto en una venta.");
            }

            if (detalleDTO.getCantidad() <= 0 ||
                    detalleDTO.getPrecioUnitario() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad y el precio deben ser mayores a cero.");
            }

            InventarioDTO producto = inventarioService.listarInventario().stream()
                    .filter(item -> item.getCodigoProducto()
                            .equalsIgnoreCase(detalleDTO.getCodigoProducto()))
                    .findFirst()
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "El producto no existe en el inventario.")
                    );

            if (producto.getStock() < detalleDTO.getCantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto: "
                                + producto.getNombreProducto());
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

        // Descontamos el stock.
        for (DetalleVenta detalle : detalles) {

            MovimientoInventarioDTO movimiento = new MovimientoInventarioDTO();

            movimiento.setCodigoProducto(detalle.getCodigoProducto());
            movimiento.setTipo("SALIDA");
            movimiento.setCantidad(detalle.getCantidad());
            movimiento.setMotivo("Venta " + venta.getNumeroVenta());

            boolean registrado = inventarioService.registrarMovimiento(movimiento);

            if (!registrado) {
                throw new IllegalArgumentException(
                        "No se pudo descontar el stock del producto: "
                                + detalle.getCodigoProducto());
            }
        }

        // Generamos el comprobante.
        comprobanteService.registrarComprobante(
                venta,
                datos.getTipoComprobante(),
                datos.getNumeroDocumento()
        );

        // Guardamos la venta y su identificador de solicitud.
        ventas.add(venta);
        solicitudesProcesadas.put(idSolicitud, venta);

        siguienteId++;

        return venta;
    }

    // Anular una venta y devolver sus productos al inventario.
    public synchronized boolean anularVenta(Long id) {

        Venta venta = buscarVenta(id);

        if (venta == null) {
            throw new IllegalArgumentException("Venta no encontrada.");
        }

        if ("ANULADA".equalsIgnoreCase(venta.getEstado())) {
            throw new IllegalArgumentException("La venta ya está anulada.");
        }

        Comprobante comprobante =
                comprobanteService.buscarPorVentaId(venta.getId());

        if (comprobante == null) {
            throw new IllegalArgumentException(
                    "No se encontró el comprobante de la venta.");
        }

        if ("ANULADO".equalsIgnoreCase(comprobante.getEstado())) {
            throw new IllegalArgumentException(
                    "El comprobante de esta venta ya está anulado.");
        }

        // Comprobamos todos los productos antes de devolver el stock.
        for (DetalleVenta detalle : venta.getDetalles()) {

            if (detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad del producto no es válida.");
            }

            boolean existe = inventarioService.listarInventario().stream()
                    .anyMatch(item -> item.getCodigoProducto()
                            .equalsIgnoreCase(detalle.getCodigoProducto()));

            if (!existe) {
                throw new IllegalArgumentException(
                        "No se encontró el producto: "
                                + detalle.getCodigoProducto());
            }
        }

        // Devolvemos el stock.
        for (DetalleVenta detalle : venta.getDetalles()) {

            boolean devuelto = inventarioService.devolverStock(
                    detalle.getCodigoProducto(),
                    detalle.getCantidad(),
                    "Anulación de venta " + venta.getNumeroVenta()
            );

            if (!devuelto) {
                throw new IllegalArgumentException(
                        "No se pudo devolver el producto: "
                                + detalle.getCodigoProducto());
            }
        }

        // Anulamos el comprobante.
        boolean comprobanteAnulado =
                comprobanteService.anularComprobantePorVenta(venta.getId());

        if (!comprobanteAnulado) {
            throw new IllegalArgumentException(
                    "No se pudo anular el comprobante.");
        }

        venta.setEstado("ANULADA");

        return true;
    }
}
