
package com.nexuserp.backend.clientesproveedores.service;

import com.nexuserp.backend.clientesproveedores.dto.ProveedorDTO;
import com.nexuserp.backend.clientesproveedores.model.Proveedor;
import com.nexuserp.backend.clientesproveedores.repository.ProveedorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    // Registrar proveedores iniciales si la tabla está vacía
    @PostConstruct
    public void cargarProveedoresIniciales() {

        if (proveedorRepository.count() == 0) {

            registrarProveedorInicial(
                    "RUC", "20123456789",
                    "Tecnologia Peru SAC", "Juan Torres",
                    "987654321", "ventas@tecnologiaperu.com",
                    "Lima"
            );

            registrarProveedorInicial(
                    "RUC", "20987654321",
                    "Computadoras del Norte SAC", "Pedro Flores",
                    "912345678", "contacto@computadoras.com",
                    "Trujillo"
            );
        }
    }

    // Guardar proveedores iniciales
    private void registrarProveedorInicial(
            String tipoDocumento,
            String numeroDocumento,
            String razonSocial,
            String nombreContacto,
            String telefono,
            String correo,
            String direccion) {

        Proveedor proveedor = new Proveedor();

        proveedor.setTipoDocumento(tipoDocumento);
        proveedor.setNumeroDocumento(numeroDocumento);
        proveedor.setRazonSocial(razonSocial);
        proveedor.setNombreContacto(nombreContacto);
        proveedor.setTelefono(telefono);
        proveedor.setCorreo(correo);
        proveedor.setDireccion(direccion);
        proveedor.setActivo(true);

        proveedorRepository.save(proveedor);
    }

    // Listar proveedores activos
    public List<Proveedor> listarProveedores() {

        return proveedorRepository.findAll()
                .stream()
                .filter(Proveedor::isActivo)
                .toList();
    }

    // Buscar proveedor por ID
    public Proveedor buscarProveedor(Long id) {

        Proveedor proveedor = proveedorRepository.findById(id)
                .orElse(null);

        if (proveedor == null || !proveedor.isActivo()) {
            return null;
        }

        return proveedor;
    }

    // Registrar proveedor
    public Proveedor registrarProveedor(ProveedorDTO datos) {

        validarDatos(datos);

        boolean documentoExiste = proveedorRepository.findAll()
                .stream()
                .anyMatch(proveedor ->
                        proveedor.getNumeroDocumento().equals(datos.getNumeroDocumento())
                                && proveedor.isActivo()
                );

        if (documentoExiste) {
            throw new IllegalArgumentException(
                    "Ya existe un proveedor con ese número de documento."
            );
        }

        Proveedor proveedor = new Proveedor();

        proveedor.setTipoDocumento(datos.getTipoDocumento());
        proveedor.setNumeroDocumento(datos.getNumeroDocumento());
        proveedor.setRazonSocial(datos.getRazonSocial());
        proveedor.setNombreContacto(datos.getNombreContacto());
        proveedor.setTelefono(datos.getTelefono());
        proveedor.setCorreo(datos.getCorreo());
        proveedor.setDireccion(datos.getDireccion());
        proveedor.setActivo(true);

        return proveedorRepository.save(proveedor);
    }

    // Actualizar proveedor
    public Proveedor actualizarProveedor(Long id, ProveedorDTO datos) {

        Proveedor proveedor = buscarProveedor(id);

        if (proveedor == null) {
            return null;
        }

        validarDatos(datos);

        boolean documentoExiste = proveedorRepository.findAll()
                .stream()
                .anyMatch(otro ->
                        !otro.getId().equals(id)
                                && otro.getNumeroDocumento().equals(datos.getNumeroDocumento())
                                && otro.isActivo()
                );

        if (documentoExiste) {
            throw new IllegalArgumentException(
                    "Otro proveedor ya utiliza ese número de documento."
            );
        }

        proveedor.setTipoDocumento(datos.getTipoDocumento());
        proveedor.setNumeroDocumento(datos.getNumeroDocumento());
        proveedor.setRazonSocial(datos.getRazonSocial());
        proveedor.setNombreContacto(datos.getNombreContacto());
        proveedor.setTelefono(datos.getTelefono());
        proveedor.setCorreo(datos.getCorreo());
        proveedor.setDireccion(datos.getDireccion());

        return proveedorRepository.save(proveedor);
    }

    // Desactivar proveedor
    public boolean desactivarProveedor(Long id) {

        Proveedor proveedor = buscarProveedor(id);

        if (proveedor == null) {
            return false;
        }

        proveedor.setActivo(false);

        proveedorRepository.save(proveedor);

        return true;
    }

    // Validar datos
    private void validarDatos(ProveedorDTO datos) {

        if (datos.getTipoDocumento() == null ||
                datos.getTipoDocumento().isBlank()) {
            throw new IllegalArgumentException("Debe indicar el tipo de documento.");
        }

        if (datos.getNumeroDocumento() == null ||
                datos.getNumeroDocumento().isBlank()) {
            throw new IllegalArgumentException("Debe indicar el número de documento.");
        }

        if (datos.getRazonSocial() == null ||
                datos.getRazonSocial().isBlank()) {
            throw new IllegalArgumentException("Debe indicar la razón social.");
        }
    }
}
