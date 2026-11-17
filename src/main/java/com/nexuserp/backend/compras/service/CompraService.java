
package com.nexuserp.backend.compras.service;

import com.nexuserp.backend.compras.dto.CompraDTO;
import com.nexuserp.backend.compras.dto.DetalleCompraDTO;
import com.nexuserp.backend.compras.model.Compra;
import com.nexuserp.backend.compras.model.DetalleCompra;
import com.nexuserp.backend.compras.repository.CompraRepository;
import com.nexuserp.backend.inventarioalmacen.dto.MovimientoInventarioDTO;
import com.nexuserp.backend.inventarioalmacen.service.InventarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final InventarioService inventarioService;

    public CompraService(CompraRepository compraRepository,
                         InventarioService inventarioService) {
        this.compraRepository = compraRepository;
        this.inventarioService = inventarioService;
    }

    public List<Compra> listarCompras() {
        return compraRepository.findAll();
    }

    public Compra buscarCompra(Long id) {
        return compraRepository.findById(id).orElse(null);
    }

    @Transactional
    public Compra registrarCompra(CompraDTO datos) {

        if (datos == null) {
            throw new IllegalArgumentException("Debe proporcionar los datos de la compra.");
        }

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
                throw new IllegalArgumentException(
                        "La cantidad y el precio deben ser mayores a cero."
                );
            }

            DetalleCompra detalle = new DetalleCompra(
                    null,
                    detalleDTO.getCodigoProducto(),
                    obtenerNombreProducto(detalleDTO.getCodigoProducto()),
                    detalleDTO.getCantidad(),
                    detalleDTO.getPrecioUnitario()
            );

            detalles.add(detalle);
        }

        Compra compra = new Compra(
                null,
                generarNumeroCompra(),
                datos.getProveedor(),
                LocalDateTime.now(),
                detalles,
                "REGISTRADA"
        );

        for (DetalleCompra detalle : detalles) {

            MovimientoInventarioDTO movimiento = new MovimientoInventarioDTO();

            movimiento.setCodigoProducto(detalle.getCodigoProducto());
            movimiento.setTipo("ENTRADA");
            movimiento.setCantidad(detalle.getCantidad());
            movimiento.setMotivo("Compra de mercadería");
            boolean registrado = inventarioService.registrarMovimiento(movimiento);

            if (!registrado) {
                throw new IllegalArgumentException(
                        "No se pudo registrar la entrada del producto: "
                                + detalle.getCodigoProducto()
                );
            }

            detalle.setCompra(compra);
        }

        return compraRepository.save(compra);
    }

    private String generarNumeroCompra() {

        Long siguienteNumero = compraRepository.obtenerSiguienteNumeroCompra();

        return String.format("COMP-%04d", siguienteNumero);
    }

    private String obtenerNombreProducto(String codigoProducto) {

        return inventarioService.listarInventario().stream()
                .filter(producto -> producto.getCodigoProducto()
                        .equalsIgnoreCase(codigoProducto.trim()))
                .map(producto -> producto.getNombreProducto())
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El producto no existe en el inventario."
                        )
                );
    }
}
