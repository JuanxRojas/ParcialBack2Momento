package com.compensar.inventario.dao;

import com.compensar.inventario.model.entity.Pedido;
import com.compensar.inventario.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class PedidoDAO extends GenericDAO<Pedido> {

    public PedidoDAO() {
        super(Pedido.class);
    }

    // Buscar pedidos por cliente
    public List<Pedido> findByCliente(Long idCliente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT p FROM Pedido p WHERE p.cliente.idCliente = :id",
                Pedido.class)
                .setParameter("id", idCliente)
                .getResultList();
        } finally {
            em.close();
        }
    }

    // Buscar pedidos por estado
    public List<Pedido> findByEstado(String estado) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT p FROM Pedido p WHERE p.estado = :estado",
                Pedido.class)
                .setParameter("estado", estado)
                .getResultList();
        } finally {
            em.close();
        }
    }
}