
package com.nexuserp.backend.productoscategorias.repository;

import com.nexuserp.backend.productoscategorias.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

}
