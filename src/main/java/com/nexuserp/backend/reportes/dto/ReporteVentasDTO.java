
package com.nexuserp.backend.reportes.dto;

import java.time.LocalDate;

public class ReporteVentasDTO {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer cantidadVentas;
    private Double totalVentas;
    private Double totalIGV;

    public ReporteVentasDTO() {
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

    public Integer getCantidadVentas() {
        return cantidadVentas;
    }

    public void setCantidadVentas(Integer cantidadVentas) {
        this.cantidadVentas = cantidadVentas;
    }

    public Double getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(Double totalVentas) {
        this.totalVentas = totalVentas;
    }

    public Double getTotalIGV() {
        return totalIGV;
    }

    public void setTotalIGV(Double totalIGV) {
        this.totalIGV = totalIGV;
    }
}
