package com.compensar.inventario.dao;

import com.compensar.inventario.model.entity.DetallePedido;
import com.compensar.inventario.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class DetallePedidoDAO extends GenericDAO<DetallePedido> {

    public DetallePedidoDAO() {
        super(DetallePedido.class);
    }

    // Buscar detalles por pedido
    public List<DetallePedido> findByPedido(Long idPedido) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                "SELECT d FROM DetallePedido d WHERE d.pedido.idPedido = :id",
                DetallePedido.class)
                .setParameter("id", idPedido)
                .getResultList();
        } finally {
            em.close();
        }
    }
}