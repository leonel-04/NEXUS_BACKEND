
package com.nexuserp.backend.usuariosroles.repository;

import com.nexuserp.backend.usuariosroles.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

}
