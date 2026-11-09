
package com.nexuserp.backend.ventascomprobantes.model;

import java.time.LocalDateTime;

public class Comprobante {

    private Long id;
    private String numeroComprobante;
    private String tipo;
    private String cliente;
    private String numeroDocumento;
    private LocalDateTime fecha;
    private double subtotal;
    private double igv;
    private double total;

    public Comprobante() {
    }

    public Comprobante(Long id, String numeroComprobante, String tipo,
                       String cliente, String numeroDocumento,
                       LocalDateTime fecha, double subtotal,
                       double igv, double total) {
        this.id = id;
        this.numeroComprobante = numeroComprobante;
        this.tipo = tipo;
        this.cliente = cliente;
        this.numeroDocumento = numeroDocumento;
        this.fecha = fecha;
        this.subtotal = subtotal;
        this.igv = igv;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIgv() {
        return igv;
    }

    public void setIgv(double igv) {
        this.igv = igv;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
