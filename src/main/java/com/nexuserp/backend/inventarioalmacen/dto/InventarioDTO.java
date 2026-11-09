
package com.nexuserp.backend.inventarioalmacen.dto;

public class InventarioDTO {

    private int id;
    private String codigoProducto;
    private String nombreProducto;
    private int stock;
    private int stockMinimo;
    private boolean stockBajo;

    public InventarioDTO(int id, String codigoProducto,
                         String nombreProducto, int stock,
                         int stockMinimo, boolean stockBajo) {

        this.id = id;
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.stockBajo = stockBajo;
    }

    public int getId() { return id; }
    public String getCodigoProducto() { return codigoProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public int getStock() { return stock; }
    public int getStockMinimo() { return stockMinimo; }
    public boolean isStockBajo() { return stockBajo; }
}
