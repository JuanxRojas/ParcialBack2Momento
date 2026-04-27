package com.compensar.inventario.controller;

import com.compensar.inventario.model.entity.Cliente;
import com.compensar.inventario.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/clientes/*")
public class ClienteController extends HttpServlet {

    private ClienteService clienteService = new ClienteService();
    private ObjectMapper mapper = new ObjectMapper();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Cliente> lista = clienteService.listarClientes();
                resp.getWriter().write(mapper.writeValueAsString(lista));
            } else {
                Long id = Long.parseLong(pathInfo.substring(1));
                Cliente cliente = clienteService.obtenerCliente(id);
                resp.getWriter().write(mapper.writeValueAsString(cliente));
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
            Cliente cliente = mapper.readValue(req.getInputStream(), Cliente.class);
            clienteService.crearCliente(cliente);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(mapper.writeValueAsString(cliente));
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
            Cliente cliente = mapper.readValue(req.getInputStream(), Cliente.class);
            cliente.setIdCliente(id);
            Cliente actualizado = clienteService.actualizarCliente(cliente);
            resp.getWriter().write(mapper.writeValueAsString(actualizado));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            Long id = Long.parseLong(req.getPathInfo().substring(1));
            clienteService.eliminarCliente(id);
            resp.getWriter().write("{\"mensaje\":\"Cliente eliminado correctamente\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}