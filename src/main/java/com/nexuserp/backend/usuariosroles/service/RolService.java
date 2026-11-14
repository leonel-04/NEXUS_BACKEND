
package com.nexuserp.backend.usuariosroles.service;

import com.nexuserp.backend.usuariosroles.dto.RolDTO;
import com.nexuserp.backend.usuariosroles.model.Rol;
import com.nexuserp.backend.usuariosroles.repository.RolRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @PostConstruct
    public void cargarRolesIniciales() {

        if (rolRepository.count() == 0) {

            registrarRolInicial(
                    "Administrador",
                    "Acceso a la administración del sistema"
            );

            registrarRolInicial(
                    "Usuario",
                    "Acceso a las funciones generales del sistema"
            );

            registrarRolInicial(
                    "Moderador",
                    "Acceso a funciones de supervisión"
            );
        }
    }

    private void registrarRolInicial(String nombre, String descripcion) {

        Rol rol = new Rol();

        rol.setNombre(nombre);
        rol.setDescripcion(descripcion);
        rol.setActivo(true);

        rolRepository.save(rol);
    }

    public List<Rol> listarRoles() {

        return rolRepository.findAll().stream()
                .filter(Rol::isActivo)
                .toList();
    }

    public Rol buscarRol(Long id) {

        return rolRepository.findById(id)
                .filter(Rol::isActivo)
                .orElseThrow(() ->
                        new IllegalArgumentException("Rol no encontrado")
                );
    }

    public Rol registrarRol(RolDTO dto) {

        validarRol(dto);

        boolean existe = rolRepository.findAll().stream()
                .anyMatch(rol ->
                        rol.getNombre().equalsIgnoreCase(dto.getNombre().trim())
                                && rol.isActivo()
                );

        if (existe) {
            throw new IllegalArgumentException("El rol ya existe");
        }

        Rol rol = new Rol();

        rol.setNombre(dto.getNombre().trim());
        rol.setDescripcion(dto.getDescripcion().trim());
        rol.setActivo(true);

        return rolRepository.save(rol);
    }

    public Rol actualizarRol(Long id, RolDTO dto) {

        Rol rol = buscarRol(id);

        validarRol(dto);

        boolean existe = rolRepository.findAll().stream()
                .anyMatch(actual ->
                        !actual.getId().equals(id)
                                && actual.isActivo()
                                && actual.getNombre().equalsIgnoreCase(
                                dto.getNombre().trim()
                        )
                );

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe otro rol con ese nombre"
            );
        }

        rol.setNombre(dto.getNombre().trim());
        rol.setDescripcion(dto.getDescripcion().trim());

        return rolRepository.save(rol);
    }

    public void desactivarRol(Long id) {

        Rol rol = buscarRol(id);
        rol.setActivo(false);

        rolRepository.save(rol);
    }

    private void validarRol(RolDTO dto) {

        if (dto == null
                || dto.getNombre() == null
                || dto.getNombre().isBlank()
                || dto.getDescripcion() == null
                || dto.getDescripcion().isBlank()) {

            throw new IllegalArgumentException(
                    "El nombre y la descripción son obligatorios"
            );
        }
    }
}
