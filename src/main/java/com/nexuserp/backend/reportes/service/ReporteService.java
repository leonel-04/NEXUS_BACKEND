
package com.nexuserp.backend.reportes.service;

import com.nexuserp.backend.compras.model.Compra;
import com.nexuserp.backend.compras.service.CompraService;
import com.nexuserp.backend.inventarioalmacen.dto.InventarioDTO;
import com.nexuserp.backend.inventarioalmacen.service.InventarioService;
import com.nexuserp.backend.reportes.dto.ReporteComprasDTO;
import com.nexuserp.backend.reportes.dto.ReporteInventarioDTO;
import com.nexuserp.backend.reportes.dto.ReporteVentasDTO;
import com.nexuserp.backend.ventascomprobantes.model.Venta;
import com.nexuserp.backend.ventascomprobantes.service.VentaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReporteService {

    private final VentaService ventaService;
    private final CompraService compraService;
    private final InventarioService inventarioService;

    public ReporteService(
            VentaService ventaService,
            CompraService compraService,
            InventarioService inventarioService) {

        this.ventaService = ventaService;
        this.compraService = compraService;
        this.inventarioService = inventarioService;
    }

    public ReporteVentasDTO generarReporteVentas(
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        validarFechas(fechaInicio, fechaFin);

        List<Venta> ventas = ventaService.listarVentas()
                .stream()
                .filter(venta ->
                        "REGISTRADA".equals(venta.getEstado())
                )
                .filter(venta -> {
                    LocalDate fecha = venta.getFecha().toLocalDate();

                    return !fecha.isBefore(fechaInicio)
                            && !fecha.isAfter(fechaFin);
                })
                .toList();

        ReporteVentasDTO reporte = new ReporteVentasDTO();

        reporte.setFechaInicio(fechaInicio);
        reporte.setFechaFin(fechaFin);
        reporte.setCantidadVentas(ventas.size());

        reporte.setTotalVentas(
                ventas.stream()
                        .mapToDouble(Venta::getTotal)
                        .sum()
        );

        reporte.setTotalIGV(
                ventas.stream()
                        .mapToDouble(Venta::getIgv)
                        .sum()
        );

        return reporte;
    }

    public ReporteComprasDTO generarReporteCompras(
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        validarFechas(fechaInicio, fechaFin);

        List<Compra> compras = compraService.listarCompras()
                .stream()
                .filter(compra ->
                        "REGISTRADA".equals(compra.getEstado())
                )
                .filter(compra -> {
                    LocalDate fecha = compra.getFecha().toLocalDate();
                    return !fecha.isBefore(fechaInicio)
                            && !fecha.isAfter(fechaFin);
                })
                .toList();

        ReporteComprasDTO reporte = new ReporteComprasDTO();

        reporte.setFechaInicio(fechaInicio);
        reporte.setFechaFin(fechaFin);
        reporte.setCantidadCompras(compras.size());

        reporte.setTotalCompras(
                compras.stream()
                        .mapToDouble(Compra::getTotal)
                        .sum()
        );

        return reporte;
    }

    public ReporteInventarioDTO generarReporteInventario() {

        List<InventarioDTO> inventario =
                inventarioService.listarInventario();

        ReporteInventarioDTO reporte = new ReporteInventarioDTO();

        reporte.setTotalProductos(inventario.size());

        reporte.setProductosStockBajo(
                (int) inventario.stream()
                        .filter(producto ->
                                producto.getStock() > 0
                                        && producto.getStock()
                                        <= producto.getStockMinimo()
                        )
                        .count()
        );

        reporte.setProductosSinStock(
                (int) inventario.stream()
                        .filter(producto -> producto.getStock() == 0)
                        .count()
        );

        return reporte;
    }

    private void validarFechas(
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException(
                    "Las fechas son obligatorias"
            );
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException(
                    "La fecha de inicio no puede ser posterior a la fecha final"
            );
        }
    }
}
