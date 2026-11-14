
package com.nexuserp.backend.clientesproveedores.service;

import com.nexuserp.backend.clientesproveedores.dto.ClienteDTO;
import com.nexuserp.backend.clientesproveedores.model.Cliente;
import com.nexuserp.backend.clientesproveedores.repository.ClienteRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Registrar clientes iniciales si la tabla está vacía
    @PostConstruct
    public void cargarClientesIniciales() {

        if (clienteRepository.count() == 0) {

            registrarClienteInicial(
                    "DNI", "12345678",
                    "Carlos", "Ramirez", "987654321",
                    "carlos@gmail.com", "Lima"
            );

            registrarClienteInicial(
                    "DNI", "87654321",
                    "Maria", "Lopez", "912345678",
                    "maria@gmail.com", "Callao"
            );
        }
    }

    // Guardar clientes iniciales
    private void registrarClienteInicial(
            String tipoDocumento,
            String numeroDocumento,
            String nombres,
            String apellidos,
            String telefono,
            String correo,
            String direccion) {

        Cliente cliente = new Cliente();

        cliente.setTipoDocumento(tipoDocumento);
        cliente.setNumeroDocumento(numeroDocumento);
        cliente.setNombres(nombres);
        cliente.setApellidos(apellidos);
        cliente.setTelefono(telefono);
        cliente.setCorreo(correo);
        cliente.setDireccion(direccion);
        cliente.setActivo(true);

        clienteRepository.save(cliente);
    }

    // Listar clientes activos
    public List<Cliente> listarClientes() {

        return clienteRepository.findAll()
                .stream()
                .filter(Cliente::isActivo)
                .toList();
    }

    // Buscar cliente por ID
    public Cliente buscarCliente(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElse(null);

        if (cliente == null || !cliente.isActivo()) {
            return null;
        }

        return cliente;
    }

    // Registrar cliente
    public Cliente registrarCliente(ClienteDTO datos) {

        validarDatos(datos);

        boolean documentoExiste = clienteRepository.findAll()
                .stream()
                .anyMatch(cliente ->
                        cliente.getNumeroDocumento().equals(datos.getNumeroDocumento())
                                && cliente.isActivo()
                );

        if (documentoExiste) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente con ese número de documento."
            );
        }

        Cliente cliente = new Cliente();

        cliente.setTipoDocumento(datos.getTipoDocumento());
        cliente.setNumeroDocumento(datos.getNumeroDocumento());
        cliente.setNombres(datos.getNombres());
        cliente.setApellidos(datos.getApellidos());
        cliente.setTelefono(datos.getTelefono());
        cliente.setCorreo(datos.getCorreo());
        cliente.setDireccion(datos.getDireccion());
        cliente.setActivo(true);

        return clienteRepository.save(cliente);
    }

    // Actualizar cliente
    public Cliente actualizarCliente(Long id, ClienteDTO datos) {

        Cliente cliente = buscarCliente(id);

        if (cliente == null) {
            return null;
        }

        validarDatos(datos);

        boolean documentoExiste = clienteRepository.findAll()
                .stream()
                .anyMatch(otro ->
                        !otro.getId().equals(id)
                                && otro.getNumeroDocumento().equals(datos.getNumeroDocumento())
                                && otro.isActivo()
                );

        if (documentoExiste) {
            throw new IllegalArgumentException(
                    "Otro cliente ya utiliza ese número de documento."
            );
        }

        cliente.setTipoDocumento(datos.getTipoDocumento());
        cliente.setNumeroDocumento(datos.getNumeroDocumento());
        cliente.setNombres(datos.getNombres());
        cliente.setApellidos(datos.getApellidos());
        cliente.setTelefono(datos.getTelefono());
        cliente.setCorreo(datos.getCorreo());
        cliente.setDireccion(datos.getDireccion());

        return clienteRepository.save(cliente);
    }

    // Desactivar cliente
    public boolean desactivarCliente(Long id) {

        Cliente cliente = buscarCliente(id);

        if (cliente == null) {
            return false;
        }

        cliente.setActivo(false);

        clienteRepository.save(cliente);

        return true;
    }

    // Validar datos
    private void validarDatos(ClienteDTO datos) {

        if (datos.getTipoDocumento() == null ||
                datos.getTipoDocumento().isBlank()) {
            throw new IllegalArgumentException("Debe indicar el tipo de documento.");
        }

        if (datos.getNumeroDocumento() == null ||
                datos.getNumeroDocumento().isBlank()) {
            throw new IllegalArgumentException("Debe indicar el número de documento.");
        }

        if (datos.getNombres() == null || datos.getNombres().isBlank()) {
            throw new IllegalArgumentException("Debe indicar los nombres.");
        }

        if (datos.getApellidos() == null || datos.getApellidos().isBlank()) {
            throw new IllegalArgumentException("Debe indicar los apellidos.");
        }
    }
}
