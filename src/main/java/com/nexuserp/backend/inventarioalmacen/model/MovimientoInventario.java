
package com.nexuserp.backend.inventarioalmacen.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_inventario")
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 20)
    private String codigoProducto;

    @Column(nullable = false, length = 100)
    private String nombreProducto;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false)
    private int cantidad;

    private String motivo;

    @Column(nullable = false)
    private LocalDateTime fecha;

    public MovimientoInventario() {
    }

    public MovimientoInventario(String codigoProducto,
                                String nombreProducto,
                                String tipo,
                                int cantidad,
                                String motivo) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.fecha = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
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

    public LocalDateTime getFecha() {
        return fecha;
    }
}
