package com.compensar.inventario.controller;

import com.compensar.inventario.DTO.DetallePedidoDTO;
import com.compensar.inventario.DTO.PedidoDTO;
import com.compensar.inventario.model.entity.DetallePedido;
import com.compensar.inventario.model.entity.Pedido;
import com.compensar.inventario.model.entity.Producto;
import com.compensar.inventario.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/api/pedidos/*")
public class PedidoController extends HttpServlet {

    private PedidoService pedidoService = new PedidoService();
    private ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Pedido> lista = pedidoService.listarPedidos();
                resp.getWriter().write(mapper.writeValueAsString(lista));
            } else if (pathInfo.startsWith("/cliente/")) {
                Long idCliente = Long.parseLong(pathInfo.substring(9));
                List<Pedido> lista = pedidoService.listarPorCliente(idCliente);
                resp.getWriter().write(mapper.writeValueAsString(lista));
            } else {
                Long id = Long.parseLong(pathInfo.substring(1));
                Pedido pedido = pedidoService.obtenerPedido(id);
                resp.getWriter().write(mapper.writeValueAsString(pedido));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            PedidoDTO pedidoDTO = mapper.readValue(req.getInputStream(), PedidoDTO.class);

            List<DetallePedido> detalles = new ArrayList<>();
            for (DetallePedidoDTO dto : pedidoDTO.getDetalles()) {
                DetallePedido detalle = new DetallePedido();
                Producto producto = new Producto();
                producto.setIdProducto(dto.getIdProducto());
                detalle.setProducto(producto);
                detalle.setCantidad(dto.getCantidad());
                detalles.add(detalle);
            }

            Pedido pedido = pedidoService.crearPedido(pedidoDTO.getIdCliente(), detalles);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(mapper.writeValueAsString(pedido));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            Long id = Long.parseLong(req.getPathInfo().substring(1));
            Map<String, String> body = mapper.readValue(req.getInputStream(), Map.class);
            Pedido actualizado = pedidoService.actualizarEstado(id, body.get("estado"));
            resp.getWriter().write(mapper.writeValueAsString(actualizado));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}