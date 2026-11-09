
package com.nexuserp.backend.ventascomprobantes.model;

import java.time.LocalDateTime;
import java.util.List;

public class Venta {

    private Long id;
    private String numeroVenta;
    private String cliente;
    private LocalDateTime fecha;
    private List<DetalleVenta> detalles;
    private double subtotal;
    private double igv;
    private double total;
    private String estado;

    public Venta() {
    }

    public Venta(Long id, String numeroVenta, String cliente,
                 LocalDateTime fecha, List<DetalleVenta> detalles,
                 String estado) {
        this.id = id;
        this.numeroVenta = numeroVenta;
        this.cliente = cliente;
        this.fecha = fecha;
        this.detalles = detalles;
        this.estado = estado;
        calcularTotales();
    }

    public void calcularTotales() {
        subtotal = 0;

        if (detalles != null) {
            for (DetalleVenta detalle : detalles) {
                subtotal += detalle.getSubtotal();
            }
        }

        igv = subtotal * 0.18;
        total = subtotal + igv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroVenta() {
        return numeroVenta;
    }

    public void setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
        calcularTotales();
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getIgv() {
        return igv;
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
