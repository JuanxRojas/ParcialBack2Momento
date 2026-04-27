package com.compensar.inventario.service;

import com.compensar.inventario.dao.ProveedorDAO;
import com.compensar.inventario.model.entity.Proveedor;
import java.util.List;

public class ProveedorService {

    private ProveedorDAO proveedorDAO = new ProveedorDAO();

    public void crearProveedor(Proveedor proveedor) {
        if (proveedor.getNombre() == null || proveedor.getNombre().isBlank()) {
            throw new RuntimeException("El nombre del proveedor es obligatorio");
        }
        proveedorDAO.save(proveedor);
    }

    public Proveedor obtenerProveedor(Long id) {
        Proveedor proveedor = proveedorDAO.findById(id);
        if (proveedor == null) {
            throw new RuntimeException("Proveedor no encontrado con id: " + id);
        }
        return proveedor;
    }

    public List<Proveedor> listarProveedores() {
        return proveedorDAO.findAll();
    }

    public Proveedor actualizarProveedor(Proveedor proveedor) {
        if (proveedor.getIdProveedor() == null) {
            throw new RuntimeException("El id del proveedor es obligatorio para actualizar");
        }
        return proveedorDAO.update(proveedor);
    }

    public void eliminarProveedor(Long id) {
        proveedorDAO.delete(id);
    }
}