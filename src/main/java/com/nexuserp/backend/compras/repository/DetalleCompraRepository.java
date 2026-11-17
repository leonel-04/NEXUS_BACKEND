
package com.nexuserp.backend.compras.repository;

import com.nexuserp.backend.compras.model.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleCompraRepository
        extends JpaRepository<DetalleCompra, Long> {

}
