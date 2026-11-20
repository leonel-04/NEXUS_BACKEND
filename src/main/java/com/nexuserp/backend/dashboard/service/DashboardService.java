
package com.nexuserp.backend.dashboard.service;

import com.nexuserp.backend.dashboard.dto.DashboardResumenDTO;
import com.nexuserp.backend.productoscategorias.repository.ProductoRepository;
import com.nexuserp.backend.clientesproveedores.repository.ClienteRepository;
import com.nexuserp.backend.clientesproveedores.repository.ProveedorRepository;
import com.nexuserp.backend.ventascomprobantes.repository.VentaRepository;
import com.nexuserp.backend.compras.repository.CompraRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DashboardService {

    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final ProveedorRepository proveedorRepository;
    private final VentaRepository ventaRepository;
    private final CompraRepository compraRepository;

    public DashboardService(
            ProductoRepository productoRepository,
            ClienteRepository clienteRepository,
            ProveedorRepository proveedorRepository,
            VentaRepository ventaRepository,
            CompraRepository compraRepository) {

        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
        this.proveedorRepository = proveedorRepository;
        this.ventaRepository = ventaRepository;
        this.compraRepository = compraRepository;
    }

    public DashboardResumenDTO obtenerResumen() {

        LocalDate hoy = LocalDate.now();

        LocalDateTime inicioDia = hoy.atStartOfDay();
        LocalDateTime finDia = hoy.plusDays(1).atStartOfDay();

        LocalDate primerDiaMes = hoy.withDayOfMonth(1);
        LocalDateTime inicioMes = primerDiaMes.atStartOfDay();
        LocalDateTime finMes = primerDiaMes.plusMonths(1).atStartOfDay();

        int totalProductos = Math.toIntExact(
                productoRepository.countByActivoTrue()
        );

        int productosBajoStock = Math.toIntExact(
                productoRepository.contarProductosBajoStock()
        );

        int totalClientes = Math.toIntExact(
                clienteRepository.countByActivoTrue()
        );

        int totalProveedores = Math.toIntExact(
                proveedorRepository.countByActivoTrue()
        );

        double ventasDelDia = ventaRepository.obtenerVentasDelDia(
                inicioDia,
                finDia
        );

        double comprasDelMes = compraRepository.obtenerComprasDelMes(
                inicioMes,
                finMes
        );

        return new DashboardResumenDTO(
                totalProductos,
                productosBajoStock,
                totalClientes,
                totalProveedores,
                ventasDelDia,
                comprasDelMes
        );
    }
}
