
package com.nexuserp.backend.inventarioalmacen.dto;

public class MovimientoInventarioDTO {

    private String codigoProducto;
    private String tipo;
    private int cantidad;
    private String motivo;

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getTipo() {
        return tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
