
package com.nexuserp.backend.inventarioalmacen.service;

import com.nexuserp.backend.inventarioalmacen.model.Inventario;
import com.nexuserp.backend.inventarioalmacen.model.MovimientoInventario;
import com.nexuserp.backend.inventarioalmacen.dto.InventarioDTO;
import com.nexuserp.backend.inventarioalmacen.dto.MovimientoInventarioDTO;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventarioService {

    private final List<Inventario> inventarios = new ArrayList<>();
    private final List<MovimientoInventario> movimientos = new ArrayList<>();

    private int siguienteIdMovimiento = 1;

    public InventarioService() {

        inventarios.add(new Inventario(1, "PROD-001", "Teclado", 30, 5));
        inventarios.add(new Inventario(2, "PROD-002", "Mouse", 50, 10));
        inventarios.add(new Inventario(3, "PROD-003", "Monitor", 4, 5));
    }

    public List<InventarioDTO> listarInventario() {

        List<InventarioDTO> resultado = new ArrayList<>();

        for (Inventario inventario : inventarios) {

            resultado.add(convertirDTO(inventario));
        }

        return resultado;
    }

    public List<InventarioDTO> listarStockBajo() {

        List<InventarioDTO> resultado = new ArrayList<>();

        for (Inventario inventario : inventarios) {

            if (inventario.isStockBajo()) {
                resultado.add(convertirDTO(inventario));
            }
        }

        return resultado;
    }

    public List<MovimientoInventario> listarMovimientos() {
        return movimientos;
    }

    public boolean registrarMovimiento(MovimientoInventarioDTO dto) {

        Inventario inventario = buscarPorCodigo(dto.getCodigoProducto());

        if (inventario == null || dto.getCantidad() <= 0
                || dto.getTipo() == null) {
            return false;
        }

        String tipo = dto.getTipo().toUpperCase();

        if (!tipo.equals("ENTRADA")
                && !tipo.equals("SALIDA")
                && !tipo.equals("AJUSTE")) {
            return false;
        }

        if (tipo.equals("SALIDA")
                && inventario.getStock() < dto.getCantidad()) {
            return false;
        }

        if (tipo.equals("ENTRADA")) {
            inventario.setStock(inventario.getStock() + dto.getCantidad());
        }

        if (tipo.equals("SALIDA")) {
            inventario.setStock(inventario.getStock() - dto.getCantidad());
        }

        if (tipo.equals("AJUSTE")) {
            inventario.setStock(dto.getCantidad());
        }

        movimientos.add(new MovimientoInventario(
                siguienteIdMovimiento++,
                inventario.getCodigoProducto(),
                inventario.getNombreProducto(),
                tipo,
                dto.getCantidad(),
                dto.getMotivo()
        ));

        return true;
    }

    private Inventario buscarPorCodigo(String codigo) {

        for (Inventario inventario : inventarios) {

            if (inventario.getCodigoProducto().equalsIgnoreCase(codigo)) {
                return inventario;
            }
        }

        return null;
    }

    private InventarioDTO convertirDTO(Inventario inventario) {

        return new InventarioDTO(
                inventario.getId(),
                inventario.getCodigoProducto(),
                inventario.getNombreProducto(),
                inventario.getStock(),
                inventario.getStockMinimo(),
                inventario.isStockBajo()
        );
    }
}
