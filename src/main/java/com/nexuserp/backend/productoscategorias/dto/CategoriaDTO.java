
package com.nexuserp.backend.productoscategorias.dto;

public class CategoriaDTO {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Boolean activo;

    public CategoriaDTO() {
    }

    public CategoriaDTO(Integer id, String nombre,
                        String descripcion, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Boolean isActivo() {
        return activo;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
