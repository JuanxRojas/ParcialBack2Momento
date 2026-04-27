package com.compensar.inventario.service;

import com.compensar.inventario.dao.HistorialEntregaDAO;
import com.compensar.inventario.model.entity.HistorialEntrega;
import java.util.List;

public class HistorialEntregaService {

    private HistorialEntregaDAO historialDAO = new HistorialEntregaDAO();

    public List<HistorialEntrega> listarTodos() {
        return historialDAO.findAll();
    }

    public List<HistorialEntrega> listarPorPedido(Long idPedido) {
        return historialDAO.findByPedido(idPedido);
    }

    public List<HistorialEntrega> listarPorCliente(Long idCliente) {
        return historialDAO.findByCliente(idCliente);
    }

    public HistorialEntrega actualizarEstado(Long idEntrega, String nuevoEstado) {
        HistorialEntrega historial = historialDAO.findById(idEntrega);
        if (historial == null) {
            throw new RuntimeException("Entrega no encontrada con id: " + idEntrega);
        }
        historial.setEstadoEntrega(nuevoEstado);
        return historialDAO.update(historial);
    }
}