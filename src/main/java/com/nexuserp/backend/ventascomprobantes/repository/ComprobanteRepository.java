
package com.nexuserp.backend.ventascomprobantes.repository;

import com.nexuserp.backend.ventascomprobantes.model.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {

    boolean existsByNumeroComprobante(String numeroComprobante);

    Comprobante findByVentaId(Long ventaId);

    @Query(value = "SELECT nextval('secuencia_comprobantes')", nativeQuery = true)
    Long obtenerSiguienteNumeroComprobante();
}
