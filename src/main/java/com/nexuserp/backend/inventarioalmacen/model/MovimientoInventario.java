
package com.nexuserp.backend.inventarioalmacen.model;

import java.time.LocalDateTime;

public class MovimientoInventario {

    private int id;
    private String codigoProducto;
    private String nombreProducto;
    private String tipo;
    private int cantidad;
    private String motivo;
    private LocalDateTime fecha;

    public MovimientoInventario(int id, String codigoProducto,
                                String nombreProducto, String tipo,
                                int cantidad, String motivo) {

        this.id = id;
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.fecha = LocalDateTime.now();
    }

    public int getId() { return id; }
    public String getCodigoProducto() { return codigoProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public String getTipo() { return tipo; }
    public int getCantidad() { return cantidad; }
    public String getMotivo() { return motivo; }
    public LocalDateTime getFecha() { return fecha; }
}
