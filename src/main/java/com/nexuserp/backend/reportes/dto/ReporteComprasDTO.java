
package com.nexuserp.backend.reportes.dto;

import java.time.LocalDate;

public class ReporteComprasDTO {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer cantidadCompras;
    private Double totalCompras;

    public ReporteComprasDTO() {
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getCantidadCompras() {
        return cantidadCompras;
    }

    public void setCantidadCompras(Integer cantidadCompras) {
        this.cantidadCompras = cantidadCompras;
    }

    public Double getTotalCompras() {
        return totalCompras;
    }

    public void setTotalCompras(Double totalCompras) {
        this.totalCompras = totalCompras;
    }
}
