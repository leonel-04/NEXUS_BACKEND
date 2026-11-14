
package com.nexuserp.backend.ventascomprobantes.service;

import com.nexuserp.backend.ventascomprobantes.model.Comprobante;
import com.nexuserp.backend.ventascomprobantes.model.Venta;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComprobanteService {

    private final List<Comprobante> comprobantes = new ArrayList<>();

    private Long siguienteId = 1L;

    public List<Comprobante> listarComprobantes() {
        return comprobantes;
    }

    public Comprobante buscarPorVentaId(Long ventaId) {

        return comprobantes.stream()
                .filter(comprobante ->
                        comprobante.getVentaId().equals(ventaId))
                .findFirst()
                .orElse(null);
    }

    public Comprobante registrarComprobante(
            Venta venta,
            String tipo,
            String numeroDocumento) {

        String numero = String.format("%06d", siguienteId);

        String prefijo = tipo.equalsIgnoreCase("FACTURA")
                ? "F001-" : "B001-";

        Comprobante comprobante = new Comprobante(
                siguienteId,
                venta.getId(),
                prefijo + numero,
                tipo.toUpperCase(),
                venta.getCliente(),
                numeroDocumento,
                venta.getFecha(),
                venta.getSubtotal(),
                venta.getIgv(),
                venta.getTotal()
        );

        comprobantes.add(comprobante);
        siguienteId++;

        return comprobante;
    }

    public boolean anularComprobantePorVenta(Long ventaId) {

        Comprobante comprobante = buscarPorVentaId(ventaId);

        if (comprobante == null) {
            return false;
        }

        if ("ANULADO".equalsIgnoreCase(comprobante.getEstado())) {
            return false;
        }

        comprobante.setEstado("ANULADO");

        return true;
    }
}
