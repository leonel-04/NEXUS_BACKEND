
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

    public Comprobante registrarComprobante(Venta venta,
                                            String tipo,
                                            String numeroDocumento) {

        String numero = String.format("%06d", siguienteId);

        String prefijo = tipo.equalsIgnoreCase("FACTURA") ? "F001-" : "B001-";

        Comprobante comprobante = new Comprobante(
                siguienteId,
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
}
