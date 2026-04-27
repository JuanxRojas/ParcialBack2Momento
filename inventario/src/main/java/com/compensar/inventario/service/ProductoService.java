package com.compensar.inventario.service;

import com.compensar.inventario.dao.ProductoDAO;
import com.compensar.inventario.dao.ProveedorDAO;
import com.compensar.inventario.model.entity.Producto;
import com.compensar.inventario.model.entity.Proveedor;
import java.util.List;

public class ProductoService {

    private ProductoDAO productoDAO = new ProductoDAO();
    private ProveedorDAO proveedorDAO = new ProveedorDAO();

    public void crearProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new RuntimeException("El nombre del producto es obligatorio");
        }
        if (producto.getPrecio() == null) {
            throw new RuntimeException("El precio del producto es obligatorio");
        }
        if (producto.getProveedor() == null || producto.getProveedor().getIdProveedor() == null) {
            throw new RuntimeException("El proveedor del producto es obligatorio");
        }
        // Verificar que el proveedor exista
        Proveedor proveedor = proveedorDAO.findById(producto.getProveedor().getIdProveedor());
        if (proveedor == null) {
            throw new RuntimeException("Proveedor no encontrado");
        }
        producto.setProveedor(proveedor);
        productoDAO.save(producto);
    }

    public Producto obtenerProducto(Long id) {
        Producto producto = productoDAO.findById(id);
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
        return producto;
    }

    public List<Producto> listarProductos() {
        return productoDAO.findAll();
    }

    public List<Producto> listarPorProveedor(Long idProveedor) {
        return productoDAO.findByProveedor(idProveedor);
    }

    public Producto actualizarProducto(Producto producto) {
        if (producto.getIdProducto() == null) {
            throw new RuntimeException("El id del producto es obligatorio para actualizar");
        }
        return productoDAO.update(producto);
    }

    public void eliminarProducto(Long id) {
        productoDAO.delete(id);
    }
}