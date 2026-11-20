
package com.nexuserp.backend.ventascomprobantes.service;

import com.nexuserp.backend.ventascomprobantes.dto.DetalleVentaDTO;
import com.nexuserp.backend.ventascomprobantes.dto.VentaDTO;
import com.nexuserp.backend.ventascomprobantes.model.Comprobante;
import com.nexuserp.backend.ventascomprobantes.model.DetalleVenta;
import com.nexuserp.backend.ventascomprobantes.model.Venta;
import com.nexuserp.backend.ventascomprobantes.repository.VentaRepository;

import com.nexuserp.backend.inventarioalmacen.dto.InventarioDTO;
import com.nexuserp.backend.inventarioalmacen.dto.MovimientoInventarioDTO;
import com.nexuserp.backend.inventarioalmacen.service.InventarioService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final InventarioService inventarioService;
    private final ComprobanteService comprobanteService;

    public VentaService(VentaRepository ventaRepository,
                        InventarioService inventarioService,
                        ComprobanteService comprobanteService) {

        this.ventaRepository = ventaRepository;
        this.inventarioService = inventarioService;
        this.comprobanteService = comprobanteService;
    }

    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    public Venta buscarVenta(Long id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @Transactional
    public Venta registrarVenta(VentaDTO datos) {

        if (datos == null || datos.getIdSolicitud() == null
                || datos.getIdSolicitud().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El identificador de solicitud es obligatorio.");
        }

        String idSolicitud = datos.getIdSolicitud().trim();

        Venta ventaExistente = ventaRepository
                .findByIdSolicitud(idSolicitud)
                .orElse(null);

        if (ventaExistente != null) {
            return ventaExistente;
        }

        if (datos.getCliente() == null
                || datos.getCliente().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El cliente es obligatorio.");
        }

        if (datos.getNumeroDocumento() == null
                || datos.getNumeroDocumento().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El número de documento es obligatorio.");
        }

        if (datos.getTipoComprobante() == null
                || (!datos.getTipoComprobante().equalsIgnoreCase("BOLETA")
                && !datos.getTipoComprobante().equalsIgnoreCase("FACTURA"))) {
            throw new IllegalArgumentException(
                    "El tipo de comprobante debe ser BOLETA o FACTURA.");
        }

        if (datos.getDetalles() == null || datos.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "La venta debe contener al menos un producto.");
        }

        List<InventarioDTO> inventario = inventarioService.listarInventario();

        List<DetalleVenta> detalles = new ArrayList<>();
        Set<String> codigosProductos = new HashSet<>();

        for (DetalleVentaDTO dato : datos.getDetalles()) {

            if (dato.getCodigoProducto() == null
                    || dato.getCodigoProducto().trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "El código del producto es obligatorio.");
            }

            String codigo = dato.getCodigoProducto().trim();

            if (!codigosProductos.add(codigo.toUpperCase())) {
                throw new IllegalArgumentException(
                        "No se puede repetir un producto en la venta.");
            }

            if (dato.getCantidad() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad debe ser mayor que cero.");
            }

            if (dato.getPrecioUnitario() <= 0) {
                throw new IllegalArgumentException(
                        "El precio unitario debe ser mayor que cero.");
            }

            InventarioDTO producto = inventario.stream()
                    .filter(actual -> actual.getCodigoProducto()
                            .equalsIgnoreCase(codigo))
                    .findFirst()
                    .orElse(null);

            if (producto == null) {
                throw new IllegalArgumentException(
                        "El producto no existe en el inventario.");
            }

            if (producto.getStock() < dato.getCantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto: "
                                + producto.getNombreProducto());
            }

            DetalleVenta detalle = new DetalleVenta(
                    null,
                    producto.getCodigoProducto(),
                    producto.getNombreProducto(),
                    dato.getCantidad(),
                    dato.getPrecioUnitario()
            );

            detalles.add(detalle);
        }

        Long siguienteId = ventaRepository.obtenerSiguienteNumeroVenta();

        String numeroVenta = "VENT-" + String.format("%04d", siguienteId);

        Venta venta = new Venta(
                null,
                numeroVenta,
                datos.getCliente().trim(),
                LocalDateTime.now(),
                detalles,
                "REGISTRADA"
        );

        venta.setIdSolicitud(idSolicitud);

        for (DetalleVenta detalle : detalles) {
            detalle.setVenta(venta);
        }

        venta.calcularTotales();

        venta = ventaRepository.save(venta);

        for (DetalleVenta detalle : detalles) {

            MovimientoInventarioDTO movimiento =
                    new MovimientoInventarioDTO();

            movimiento.setCodigoProducto(detalle.getCodigoProducto());
            movimiento.setTipo("SALIDA");
            movimiento.setCantidad(detalle.getCantidad());
            movimiento.setMotivo("Venta " + venta.getNumeroVenta());

            if (!inventarioService.registrarMovimiento(movimiento)) {
                throw new IllegalArgumentException(
                        "No se pudo descontar el stock del producto: "
                                + detalle.getNombreProducto());
            }
        }

        comprobanteService.registrarComprobante(
                venta,
                datos.getTipoComprobante(),
                datos.getNumeroDocumento().trim()
        );

        return venta;
    }

    @Transactional
    public boolean anularVenta(Long id) {

        Venta venta = ventaRepository.findById(id).orElse(null);

        if (venta == null) {
            throw new IllegalArgumentException("Venta no encontrada.");
        }

        if ("ANULADA".equalsIgnoreCase(venta.getEstado())) {
            throw new IllegalArgumentException(
                    "La venta ya se encuentra anulada.");
        }

        Comprobante comprobante =
                comprobanteService.buscarPorVentaId(venta.getId());

        if (comprobante == null) {
            throw new IllegalArgumentException(
                    "No se encontró el comprobante de la venta.");
        }

        if ("ANULADO".equalsIgnoreCase(comprobante.getEstado())) {
            throw new IllegalArgumentException(
                    "El comprobante ya se encuentra anulado.");
        }

        List<InventarioDTO> inventario = inventarioService.listarInventario();

        for (DetalleVenta detalle : venta.getDetalles()) {

            if (detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad del producto no es válida.");
            }

            boolean productoExiste = inventario.stream()
                    .anyMatch(actual -> actual.getCodigoProducto()
                            .equalsIgnoreCase(detalle.getCodigoProducto()));

            if (!productoExiste) {
                throw new IllegalArgumentException(
                        "El producto ya no existe en el inventario: "
                                + detalle.getCodigoProducto());
            }
        }

        for (DetalleVenta detalle : venta.getDetalles()) {

            boolean devuelto = inventarioService.devolverStock(
                    detalle.getCodigoProducto(),
                    detalle.getCantidad(),
                    "Anulación de venta " + venta.getNumeroVenta()
            );

            if (!devuelto) {
                throw new IllegalArgumentException(
                        "No se pudo devolver el stock del producto: "
                                + detalle.getNombreProducto());
            }
        }

        if (!comprobanteService.anularComprobantePorVenta(venta.getId())) {
            throw new IllegalArgumentException(
                    "No se pudo anular el comprobante.");
        }

        venta.setEstado("ANULADA");

        ventaRepository.save(venta);

        return true;
    }
}
