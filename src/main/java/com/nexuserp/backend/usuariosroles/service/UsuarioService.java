
package com.nexuserp.backend.usuariosroles.service;

import com.nexuserp.backend.usuariosroles.dto.UsuarioDTO;
import com.nexuserp.backend.usuariosroles.model.Rol;
import com.nexuserp.backend.usuariosroles.model.Usuario;
import com.nexuserp.backend.usuariosroles.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolService rolService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolService rolService) {
        this.usuarioRepository = usuarioRepository;
        this.rolService = rolService;
    }

    @PostConstruct
    public void cargarUsuariosIniciales() {

        if (usuarioRepository.count() == 0) {

            registrarUsuarioInicial(
                    "Carlos",
                    "Ramirez",
                    "admin",
                    "admin@nexuserp.com",
                    1L
            );

            registrarUsuarioInicial(
                    "Maria",
                    "Lopez",
                    "mlopez",
                    "maria@nexuserp.com",
                    2L
            );
        }
    }

    private void registrarUsuarioInicial(String nombres,
                                         String apellidos,
                                         String nombreUsuario,
                                         String correo,
                                         Long rolId) {

        Rol rol = rolService.buscarRol(rolId);

        Usuario usuario = new Usuario();

        usuario.setNombres(nombres);
        usuario.setApellidos(apellidos);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setCorreo(correo);
        usuario.setRol(rol);
        usuario.setActivo(true);

        usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {

        return usuarioRepository.findAll().stream()
                .filter(Usuario::isActivo)
                .toList();
    }

    public Usuario buscarUsuario(Long id) {

        return usuarioRepository.findById(id)
                .filter(Usuario::isActivo)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuario no encontrado")
                );
    }

    public Usuario registrarUsuario(UsuarioDTO dto) {

        validarUsuario(dto);
        validarDatosUnicos(dto, null);

        Rol rol = rolService.buscarRol(dto.getRolId());

        Usuario usuario = new Usuario();

        usuario.setNombres(dto.getNombres().trim());
        usuario.setApellidos(dto.getApellidos().trim());
        usuario.setNombreUsuario(dto.getNombreUsuario().trim());
        usuario.setCorreo(dto.getCorreo().trim());
        usuario.setRol(rol);
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Long id, UsuarioDTO dto) {

        Usuario usuario = buscarUsuario(id);

        validarUsuario(dto);
        validarDatosUnicos(dto, id);

        Rol rol = rolService.buscarRol(dto.getRolId());

        usuario.setNombres(dto.getNombres().trim());
        usuario.setApellidos(dto.getApellidos().trim());
        usuario.setNombreUsuario(dto.getNombreUsuario().trim());
        usuario.setCorreo(dto.getCorreo().trim());
        usuario.setRol(rol);

        return usuarioRepository.save(usuario);
    }

    public void desactivarUsuario(Long id) {

        Usuario usuario = buscarUsuario(id);
        usuario.setActivo(false);

        usuarioRepository.save(usuario);
    }

    private void validarUsuario(UsuarioDTO dto) {

        if (dto == null
                || dto.getNombres() == null
                || dto.getNombres().isBlank()
                || dto.getApellidos() == null
                || dto.getApellidos().isBlank()
                || dto.getNombreUsuario() == null
                || dto.getNombreUsuario().isBlank()
                || dto.getCorreo() == null
                || dto.getCorreo().isBlank()
                || dto.getRolId() == null) {

            throw new IllegalArgumentException(
                    "Todos los campos son obligatorios"
            );
        }

        if (!dto.getCorreo().contains("@")) {
            throw new IllegalArgumentException("El correo no es válido");
        }
    }

    private void validarDatosUnicos(UsuarioDTO dto, Long idExcluir) {

        boolean existeNombreUsuario = usuarioRepository.findAll().stream()
                .anyMatch(usuario ->
                        usuario.isActivo()
                                && !usuario.getId().equals(idExcluir)
                                && usuario.getNombreUsuario().equalsIgnoreCase(
                                dto.getNombreUsuario().trim()
                        )
                );

        if (existeNombreUsuario) {
            throw new IllegalArgumentException(
                    "El nombre de usuario ya está registrado"
            );
        }

        boolean existeCorreo = usuarioRepository.findAll().stream()
                .anyMatch(usuario ->
                        usuario.isActivo()
                                && !usuario.getId().equals(idExcluir)
                                && usuario.getCorreo().equalsIgnoreCase(
                                dto.getCorreo().trim()
                        )
                );

        if (existeCorreo) {
            throw new IllegalArgumentException(
                    "El correo ya está registrado"
            );
        }
    }
}
