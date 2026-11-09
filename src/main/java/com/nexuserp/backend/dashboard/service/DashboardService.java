
package com.nexuserp.backend.dashboard.service;

import com.nexuserp.backend.dashboard.dto.DashboardResumenDTO;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    public DashboardResumenDTO obtenerResumen() {

        int totalProductos = 150;
        int productosBajoStock = 12;
        int totalClientes = 85;
        int totalProveedores = 20;

        double ventasDelDia = 2450.50;
        double comprasDelMes = 8750.00;

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
