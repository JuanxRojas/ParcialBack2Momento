package com.compensar.inventario.controller;

import com.compensar.inventario.model.entity.HistorialEntrega;
import com.compensar.inventario.service.HistorialEntregaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/historial/*")
public class HistorialEntregaController extends HttpServlet {

    private HistorialEntregaService historialService = new HistorialEntregaService();
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
                List<HistorialEntrega> lista = historialService.listarTodos();
                resp.getWriter().write(mapper.writeValueAsString(lista));
            } else if (pathInfo.startsWith("/cliente/")) {
                Long idCliente = Long.parseLong(pathInfo.substring(9));
                List<HistorialEntrega> lista = historialService.listarPorCliente(idCliente);
                resp.getWriter().write(mapper.writeValueAsString(lista));
            } else if (pathInfo.startsWith("/pedido/")) {
                Long idPedido = Long.parseLong(pathInfo.substring(8));
                List<HistorialEntrega> lista = historialService.listarPorPedido(idPedido);
                resp.getWriter().write(mapper.writeValueAsString(lista));
            }
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
            HistorialEntrega actualizado = historialService.actualizarEstado(id, body.get("estadoEntrega"));
            resp.getWriter().write(mapper.writeValueAsString(actualizado));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}