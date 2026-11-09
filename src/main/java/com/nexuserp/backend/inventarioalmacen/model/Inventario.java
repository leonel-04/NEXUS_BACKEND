package com.nexuserp.backend.inventarioalmacen.model;

public class Inventario {
    private int id;
    private String codigoProducto;
    private String nombreProducto;
    private int stock;
    private int stockMinimo;

    public Inventario(int id, String codigoProducto,
                      String nombreProducto, int stock, int stockMinimo) {
        this.id = id;
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    public int getId() { return id; }
    public String getCodigoProducto() { return codigoProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public int getStock() { return stock; }
    public int getStockMinimo() { return stockMinimo; }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public boolean isStockBajo() {
        return stock <= stockMinimo;
    }
}
