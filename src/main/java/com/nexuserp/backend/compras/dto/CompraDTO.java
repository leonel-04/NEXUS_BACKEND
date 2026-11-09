
package com.nexuserp.backend.compras.dto;

import java.util.List;

public class CompraDTO {

    private String proveedor;
    private List<DetalleCompraDTO> detalles;

    public CompraDTO() {
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public List<DetalleCompraDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompraDTO> detalles) {
        this.detalles = detalles;
    }
}
