package com.compensar.inventario.DTO;

import java.util.List;

public class PedidoDTO {

    private Long idCliente;
    private List<DetallePedidoDTO> detalles;

    // Getters y Setters
    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public List<DetallePedidoDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoDTO> detalles) { this.detalles = detalles; }
}