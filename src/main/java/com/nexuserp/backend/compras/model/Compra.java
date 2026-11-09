
package com.nexuserp.backend.compras.model;

import java.time.LocalDateTime;
import java.util.List;

public class Compra {

    private Long id;
    private String numeroCompra;
    private String proveedor;
    private LocalDateTime fecha;
    private List<DetalleCompra> detalles;
    private double total;
    private String estado;

    public Compra() {
    }

    public Compra(Long id, String numeroCompra, String proveedor,
                  LocalDateTime fecha, List<DetalleCompra> detalles,
                  String estado) {
        this.id = id;
        this.numeroCompra = numeroCompra;
        this.proveedor = proveedor;
        this.fecha = fecha;
        this.detalles = detalles;
        this.estado = estado;
        calcularTotal();
    }

    public void calcularTotal() {
        total = 0;

        if (detalles != null) {
            for (DetalleCompra detalle : detalles) {
                total += detalle.getSubtotal();
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroCompra() {
        return numeroCompra;
    }

    public void setNumeroCompra(String numeroCompra) {
        this.numeroCompra = numeroCompra;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public List<DetalleCompra> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompra> detalles) {
        this.detalles = detalles;
        calcularTotal();
    }

    public double getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
