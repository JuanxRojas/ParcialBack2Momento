package com.compensar.inventario.service;

import com.compensar.inventario.dao.ClienteDAO;
import com.compensar.inventario.dao.DetallePedidoDAO;
import com.compensar.inventario.dao.HistorialEntregaDAO;
import com.compensar.inventario.dao.PedidoDAO;
import com.compensar.inventario.dao.ProductoDAO;
import com.compensar.inventario.model.entity.Cliente;
import com.compensar.inventario.model.entity.DetallePedido;
import com.compensar.inventario.model.entity.HistorialEntrega;
import com.compensar.inventario.model.entity.Pedido;
import com.compensar.inventario.model.entity.Producto;
import java.time.LocalDate;
import java.util.List;

public class PedidoService {

    private PedidoDAO pedidoDAO                   = new PedidoDAO();
    private ClienteDAO clienteDAO                 = new ClienteDAO();
    private ProductoDAO productoDAO               = new ProductoDAO();
    private DetallePedidoDAO detallePedidoDAO     = new DetallePedidoDAO();
    private HistorialEntregaDAO historialDAO      = new HistorialEntregaDAO();


    public Pedido crearPedido(Long idCliente, List<DetallePedido> detalles) {


        Cliente cliente = clienteDAO.findById(idCliente);
        if (cliente == null) {
            throw new RuntimeException("Cliente no encontrado con id: " + idCliente);
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setFechaPedido(LocalDate.now());
        pedido.setEstado("pendiente");
        pedidoDAO.save(pedido);

        for (DetallePedido detalle : detalles) {
            Producto producto = productoDAO.findById(detalle.getProducto().getIdProducto());
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado con id: "
                        + detalle.getProducto().getIdProducto());
            }

            productoDAO.actualizarStock(producto.getIdProducto(), detalle.getCantidad());

            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setPrecioUnitario(producto.getPrecio());
            detallePedidoDAO.save(detalle);
        }

        HistorialEntrega historial = new HistorialEntrega();
        historial.setPedido(pedido);
        historial.setFechaEntrega(LocalDate.now());
        historial.setEstadoEntrega("pendiente");
        historialDAO.save(historial);

        return pedido;
    }

    public Pedido obtenerPedido(Long id) {
        Pedido pedido = pedidoDAO.findById(id);
        if (pedido == null) {
            throw new RuntimeException("Pedido no encontrado con id: " + id);
        }
        return pedido;
    }

    public List<Pedido> listarPedidos() {
        return pedidoDAO.findAll();
    }

    public List<Pedido> listarPorCliente(Long idCliente) {
        return pedidoDAO.findByCliente(idCliente);
    }

    public List<Pedido> listarPorEstado(String estado) {
        return pedidoDAO.findByEstado(estado);
    }

    public Pedido actualizarEstado(Long idPedido, String nuevoEstado) {
        Pedido pedido = pedidoDAO.findById(idPedido);
        if (pedido == null) {
            throw new RuntimeException("Pedido no encontrado con id: " + idPedido);
        }
        pedido.setEstado(nuevoEstado);
        return pedidoDAO.update(pedido);
    }
}