
package com.nexuserp.backend.inventarioalmacen.service;

import com.nexuserp.backend.inventarioalmacen.dto.InventarioDTO;
import com.nexuserp.backend.inventarioalmacen.dto.MovimientoInventarioDTO;
import com.nexuserp.backend.inventarioalmacen.model.MovimientoInventario;
import com.nexuserp.backend.inventarioalmacen.repository.MovimientoInventarioRepository;
import com.nexuserp.backend.productoscategorias.model.Producto;
import com.nexuserp.backend.productoscategorias.repository.ProductoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventarioService {

    private final ProductoRepository productoRepository;
    private final MovimientoInventarioRepository movimientoRepository;

    public InventarioService(ProductoRepository productoRepository,
                             MovimientoInventarioRepository movimientoRepository) {
        this.productoRepository = productoRepository;
        this.movimientoRepository = movimientoRepository;
    }

    public List<InventarioDTO> listarInventario() {

        return productoRepository.findAll().stream()
                .filter(producto -> Boolean.TRUE.equals(producto.getActivo()))
                .map(this::convertirDTO)
                .toList();
    }

    public List<InventarioDTO> listarStockBajo() {

        return productoRepository.findAll().stream()
                .filter(producto -> Boolean.TRUE.equals(producto.getActivo()))
                .filter(producto -> producto.getStock() <= producto.getStockMinimo())
                .map(this::convertirDTO)
                .toList();
    }

    public List<MovimientoInventario> listarMovimientos() {
        return movimientoRepository.findAll();
    }

    @Transactional
    public boolean registrarMovimiento(MovimientoInventarioDTO dto) {

        if (dto == null || dto.getCodigoProducto() == null
                || dto.getCantidad() <= 0 || dto.getTipo() == null) {
            return false;
        }

        Producto producto = productoRepository.findAll().stream()
                .filter(actual -> actual.getCodigo().equalsIgnoreCase(
                        dto.getCodigoProducto().trim()))
                .filter(actual -> Boolean.TRUE.equals(actual.getActivo()))
                .findFirst()
                .orElse(null);

        if (producto == null) {
            return false;
        }

        String tipo = dto.getTipo().trim().toUpperCase();

        if (!tipo.equals("ENTRADA")
                && !tipo.equals("SALIDA")
                && !tipo.equals("AJUSTE")) {
            return false;
        }

        int stockActual = producto.getStock() == null ? 0 : producto.getStock();

        if (tipo.equals("SALIDA") && stockActual < dto.getCantidad()) {
            return false;
        }

        if (tipo.equals("ENTRADA")) {
            producto.setStock(stockActual + dto.getCantidad());
        }

        if (tipo.equals("SALIDA")) {
            producto.setStock(stockActual - dto.getCantidad());
        }

        if (tipo.equals("AJUSTE")) {
            producto.setStock(dto.getCantidad());
        }

        productoRepository.save(producto);

        MovimientoInventario movimiento = new MovimientoInventario(
                producto.getCodigo(),
                producto.getNombre(),
                tipo,
                dto.getCantidad(),
                dto.getMotivo()
        );

        movimientoRepository.save(movimiento);

        return true;
    }

    public boolean devolverStock(String codigoProducto, int cantidad, String motivo) {

        MovimientoInventarioDTO movimiento = new MovimientoInventarioDTO();

        movimiento.setCodigoProducto(codigoProducto);
        movimiento.setTipo("ENTRADA");
        movimiento.setCantidad(cantidad);
        movimiento.setMotivo(motivo);

        return registrarMovimiento(movimiento);
    }

    private InventarioDTO convertirDTO(Producto producto) {

        int stock = producto.getStock() == null ? 0 : producto.getStock();
        int stockMinimo = producto.getStockMinimo() == null
                ? 0 : producto.getStockMinimo();

        return new InventarioDTO(
                producto.getId(),
                producto.getCodigo(),
                producto.getNombre(),
                stock,
                stockMinimo,
                stock <= stockMinimo
        );
    }
}
