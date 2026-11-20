
package com.nexuserp.backend.ventascomprobantes.service;

import com.nexuserp.backend.ventascomprobantes.model.Comprobante;
import com.nexuserp.backend.ventascomprobantes.model.Venta;
import com.nexuserp.backend.ventascomprobantes.repository.ComprobanteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ComprobanteService {

    private final ComprobanteRepository comprobanteRepository;

    public ComprobanteService(ComprobanteRepository comprobanteRepository) {
        this.comprobanteRepository = comprobanteRepository;
    }

    public List<Comprobante> listarComprobantes() {
        return comprobanteRepository.findAll();
    }

    public Comprobante buscarPorVentaId(Long ventaId) {
        return comprobanteRepository.findByVentaId(ventaId);
    }

    @Transactional
    public Comprobante registrarComprobante(
            Venta venta,
            String tipo,
            String numeroDocumento) {

        Long siguienteId =
                comprobanteRepository.obtenerSiguienteNumeroComprobante();

        String numero = String.format("%06d", siguienteId);

        String prefijo = tipo.equalsIgnoreCase("FACTURA")
                ? "F001-" : "B001-";

        Comprobante comprobante = new Comprobante(
                null,
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

        return comprobanteRepository.save(comprobante);
    }

    @Transactional
    public boolean anularComprobantePorVenta(Long ventaId) {

        Comprobante comprobante = buscarPorVentaId(ventaId);

        if (comprobante == null) {
            return false;
        }

        if ("ANULADO".equalsIgnoreCase(comprobante.getEstado())) {
            return false;
        }

        comprobante.setEstado("ANULADO");

        comprobanteRepository.save(comprobante);

        return true;
    }
}
