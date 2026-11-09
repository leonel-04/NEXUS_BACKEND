
package com.nexuserp.backend.productoscategorias.controller;

import com.nexuserp.backend.productoscategorias.dto.CategoriaDTO;
import com.nexuserp.backend.productoscategorias.service.CategoriaService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<CategoriaDTO> listarCategorias() {
        return categoriaService.listarCategorias();
    }

    @PostMapping
    public CategoriaDTO registrarCategoria(@RequestBody CategoriaDTO categoria) {
        return categoriaService.registrarCategoria(categoria);
    }

    @DeleteMapping("/{id}")
    public String eliminarCategoria(@PathVariable int id) {

        if (categoriaService.eliminarCategoria(id)) {
            return "Categoria desactivada correctamente";
        }

        return "Categoria no encontrada";
    }
}
