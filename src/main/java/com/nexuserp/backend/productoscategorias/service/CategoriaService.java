
package com.nexuserp.backend.productoscategorias.service;

import com.nexuserp.backend.productoscategorias.dto.CategoriaDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriaService {

    private final List<CategoriaDTO> categorias = new ArrayList<>();

    private int siguienteId = 4;

    public CategoriaService() {

        categorias.add(new CategoriaDTO(
                1, "Perifericos", "Accesorios de computadora", true));

        categorias.add(new CategoriaDTO(
                2, "Monitores", "Pantallas y monitores", true));

        categorias.add(new CategoriaDTO(
                3, "Componentes", "Componentes de computadora", true));
    }

    public List<CategoriaDTO> listarCategorias() {
        return categorias;
    }

    public CategoriaDTO registrarCategoria(CategoriaDTO categoria) {

        CategoriaDTO nuevaCategoria = new CategoriaDTO(
                siguienteId++,
                categoria.getNombre(),
                categoria.getDescripcion(),
                true
        );

        categorias.add(nuevaCategoria);

        return nuevaCategoria;
    }

    public boolean eliminarCategoria(int id) {

        for (CategoriaDTO categoria : categorias) {

            if (categoria.getId() == id) {
                categoria.setActivo(false);
                return true;
            }
        }

        return false;
    }
}
