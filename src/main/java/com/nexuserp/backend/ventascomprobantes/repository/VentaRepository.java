
package com.nexuserp.backend.ventascomprobantes.repository;

import com.nexuserp.backend.ventascomprobantes.model.Venta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    boolean existsByNumeroVenta(String numeroVenta);

    Optional<Venta> findByIdSolicitud(String idSolicitud);

    @Query(value = "SELECT nextval('secuencia_ventas')", nativeQuery = true)
    Long obtenerSiguienteNumeroVenta();

    @Query("""
            SELECT COALESCE(SUM(v.total), 0)
            FROM Venta v
            WHERE v.estado = 'REGISTRADA'
            AND v.fecha >= :inicio
            AND v.fecha < :fin
            """)
    Double obtenerVentasDelDia(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );
}
