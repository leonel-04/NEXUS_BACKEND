
package com.nexuserp.backend.compras.repository;

import com.nexuserp.backend.compras.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    boolean existsByNumeroCompra(String numeroCompra);

    @Query(value = "SELECT nextval('secuencia_compras')", nativeQuery = true)
    Long obtenerSiguienteNumeroCompra();

    @Query("""
            SELECT COALESCE(SUM(c.total), 0)
            FROM Compra c
            WHERE c.estado = 'REGISTRADA'
            AND c.fecha >= :inicio
            AND c.fecha < :fin
            """)
    Double obtenerComprasDelMes(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );
}
