
package com.nexuserp.backend.inventarioalmacen.repository;

import com.nexuserp.backend.inventarioalmacen.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoInventarioRepository
        extends JpaRepository<MovimientoInventario, Integer> {

}
