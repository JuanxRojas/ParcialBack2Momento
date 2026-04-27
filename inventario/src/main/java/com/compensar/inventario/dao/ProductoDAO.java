package com.compensar.inventario.dao;

import com.compensar.inventario.model.entity.Producto;
import com.compensar.inventario.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ProductoDAO extends GenericDAO<Producto> {

    public ProductoDAO() {
        super(Producto.class);
    }

    // Buscar productos por proveedor
    public List<Producto> findByProveedor(Long idProveedor) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT p FROM Producto p WHERE p.proveedor.idProveedor = :id",
                Producto.class)
                .setParameter("id", idProveedor)
                .getResultList();
        } finally {
            em.close();
        }
    }

    // Actualizar stock al registrar un pedido
    public void actualizarStock(Long idProducto, int cantidad) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Producto producto = em.find(Producto.class, idProducto);
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado: " + idProducto);
            }
            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }
            producto.setStock(producto.getStock() - cantidad);
            em.merge(producto);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}