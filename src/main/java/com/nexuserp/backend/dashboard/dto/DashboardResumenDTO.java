
package com.nexuserp.backend.dashboard.dto;

public class DashboardResumenDTO {

    private int totalProductos;
    private int productosBajoStock;
    private int totalClientes;
    private int totalProveedores;
    private double ventasDelDia;
    private double comprasDelMes;

    public DashboardResumenDTO(
            int totalProductos,
            int productosBajoStock,
            int totalClientes,
            int totalProveedores,
            double ventasDelDia,
            double comprasDelMes) {

        this.totalProductos = totalProductos;
        this.productosBajoStock = productosBajoStock;
        this.totalClientes = totalClientes;
        this.totalProveedores = totalProveedores;
        this.ventasDelDia = ventasDelDia;
        this.comprasDelMes = comprasDelMes;
    }

    public int getTotalProductos() {
        return totalProductos;
    }

    public int getProductosBajoStock() {
        return productosBajoStock;
    }

    public int getTotalClientes() {
        return totalClientes;
    }

    public int getTotalProveedores() {
        return totalProveedores;
    }

    public double getVentasDelDia() {
        return ventasDelDia;
    }

    public double getComprasDelMes() {
        return comprasDelMes;
    }
}
