
package com.nexuserp.backend.productoscategorias.dto;

public class ProductoDTO {

    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String categoria;
    private double precio;
    private int stock;
    private int stockMinimo;
    private boolean activo;

    public ProductoDTO(int id, String codigo, String nombre,
                       String descripcion, String categoria,
                       double precio, int stock, int stockMinimo,
                       boolean activo) {

        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.activo = activo;
    }

    public int getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getCategoria() { return categoria; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public int getStockMinimo() { return stockMinimo; }
    public boolean isActivo() { return activo; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setStock(int stock) { this.stock = stock; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
