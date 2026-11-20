
package com.nexuserp.backend.clientesproveedores.repository;

import com.nexuserp.backend.clientesproveedores.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    long countByActivoTrue();

}
