package com.nexuserp.backend.compras.repository;

import com.nexuserp.backend.compras.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    boolean existsByNumeroCompra(String numeroCompra);

    @Query(value = "SELECT nextval('secuencia_compras')", nativeQuery = true)
    Long obtenerSiguienteNumeroCompra();

}