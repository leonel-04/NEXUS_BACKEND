
package com.nexuserp.backend.reportes.dto;

public class ReporteInventarioDTO {

    private Integer totalProductos;
    private Integer productosStockBajo;
    private Integer productosSinStock;

    public ReporteInventarioDTO() {
    }

    public Integer getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(Integer totalProductos) {
        this.totalProductos = totalProductos;
    }

    public Integer getProductosStockBajo() {
        return productosStockBajo;
    }

    public void setProductosStockBajo(Integer productosStockBajo) {
        this.productosStockBajo = productosStockBajo;
    }

    public Integer getProductosSinStock() {
        return productosSinStock;
    }

    public void setProductosSinStock(Integer productosSinStock) {
        this.productosSinStock = productosSinStock;
    }
}
