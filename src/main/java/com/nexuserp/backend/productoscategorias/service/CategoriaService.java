
package com.nexuserp.backend.productoscategorias.service;

import com.nexuserp.backend.productoscategorias.dto.CategoriaDTO;
import com.nexuserp.backend.productoscategorias.model.Categoria;
import com.nexuserp.backend.productoscategorias.repository.CategoriaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Registrar categorías iniciales si la tabla está vacía
    @PostConstruct
    public void cargarCategoriasIniciales() {

        if (categoriaRepository.count() == 0) {

            registrarCategoriaInicial(
                    "Perifericos",
                    "Accesorios de computadora"
            );

            registrarCategoriaInicial(
                    "Monitores",
                    "Pantallas y monitores"
            );

            registrarCategoriaInicial(
                    "Componentes",
                    "Componentes de computadora"
            );
        }
    }

    // Guardar categorías iniciales
    private void registrarCategoriaInicial(
            String nombre,
            String descripcion) {

        Categoria categoria = new Categoria();

        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
        categoria.setActivo(true);

        categoriaRepository.save(categoria);
    }

    // Listar categorías
    public List<CategoriaDTO> listarCategorias() {

        return categoriaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    // Registrar categoría
    public CategoriaDTO registrarCategoria(CategoriaDTO datos) {

        Categoria categoria = new Categoria();

        categoria.setNombre(datos.getNombre());
        categoria.setDescripcion(datos.getDescripcion());
        categoria.setActivo(true);

        categoria = categoriaRepository.save(categoria);

        return convertirADTO(categoria);
    }

    // Modificar categoría
    public CategoriaDTO modificarCategoria(int id, CategoriaDTO datos) {

        Categoria categoria = categoriaRepository.findById(id)
                .orElse(null);

        if (categoria == null ||
                !Boolean.TRUE.equals(categoria.getActivo())) {
            return null;
        }

        categoria.setNombre(datos.getNombre());
        categoria.setDescripcion(datos.getDescripcion());

        categoria = categoriaRepository.save(categoria);

        return convertirADTO(categoria);
    }

    // Eliminar categoría (desactivación lógica)
    public boolean eliminarCategoria(int id) {

        Categoria categoria = categoriaRepository.findById(id)
                .orElse(null);

        if (categoria == null ||
                !Boolean.TRUE.equals(categoria.getActivo())) {
            return false;
        }

        categoria.setActivo(false);

        categoriaRepository.save(categoria);

        return true;
    }

    // Convertir entidad a DTO
    private CategoriaDTO convertirADTO(Categoria categoria) {

        return new CategoriaDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getActivo()
        );
    }
}
