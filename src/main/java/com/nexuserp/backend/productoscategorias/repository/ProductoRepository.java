
package com.nexuserp.backend.productoscategorias.repository;

import com.nexuserp.backend.productoscategorias.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    long countByActivoTrue();

    @Query("""
            SELECT COUNT(p)
            FROM Producto p
            WHERE p.activo = true
            AND p.stock <= p.stockMinimo
            """)
    long contarProductosBajoStock();
}
