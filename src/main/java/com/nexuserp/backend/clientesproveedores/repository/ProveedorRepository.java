
package com.nexuserp.backend.clientesproveedores.repository;

import com.nexuserp.backend.clientesproveedores.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

}
