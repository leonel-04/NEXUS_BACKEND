
package com.nexuserp.backend.productoscategorias.repository;

import com.nexuserp.backend.productoscategorias.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

}
