
package com.nexuserp.backend.productoscategorias.dto;

public class ProductoDTO {

    private Integer id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String categoria;
    private double precio;
    private Integer stock;
    private Integer stockMinimo;
    private Boolean activo;

    // Constructor vacio
    public ProductoDTO() {
    }

    // Constructor con parametros
    public ProductoDTO(Integer id, String codigo, String nombre,
                       String descripcion, String categoria,
                       double precio, Integer stock, Integer stockMinimo,
                       Boolean activo) {

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

    // Getters
    public Integer getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public Integer getStock() {
        return stock;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public Boolean isActivo() {
        return activo;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
