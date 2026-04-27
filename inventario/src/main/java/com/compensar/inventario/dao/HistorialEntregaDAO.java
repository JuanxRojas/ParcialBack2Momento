package com.compensar.inventario.dao;

import com.compensar.inventario.model.entity.HistorialEntrega;
import com.compensar.inventario.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class HistorialEntregaDAO extends GenericDAO<HistorialEntrega> {

    public HistorialEntregaDAO() {
        super(HistorialEntrega.class);
    }

    // Buscar historial por pedido
    public List<HistorialEntrega> findByPedido(Long idPedido) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT h FROM HistorialEntrega h WHERE h.pedido.idPedido = :id",
                HistorialEntrega.class)
                .setParameter("id", idPedido)
                .getResultList();
        } finally {
            em.close();
        }
    }

    // Buscar historial por cliente (join con pedido)
    public List<HistorialEntrega> findByCliente(Long idCliente) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT h FROM HistorialEntrega h WHERE h.pedido.cliente.idCliente = :id",
                HistorialEntrega.class)
                .setParameter("id", idCliente)
                .getResultList();
        } finally {
            em.close();
        }
    }
}