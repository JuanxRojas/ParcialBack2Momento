package com.compensar.inventario.controller;

import com.compensar.inventario.model.entity.Proveedor;
import com.compensar.inventario.service.ProveedorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/proveedores/*")
public class ProveedorController extends HttpServlet {

    private ProveedorService proveedorService = new ProveedorService();
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Proveedor> lista = proveedorService.listarProveedores();
                resp.getWriter().write(mapper.writeValueAsString(lista));
            } else {
                Long id = Long.parseLong(pathInfo.substring(1));
                Proveedor proveedor = proveedorService.obtenerProveedor(id);
                resp.getWriter().write(mapper.writeValueAsString(proveedor));
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
            Proveedor proveedor = mapper.readValue(req.getInputStream(), Proveedor.class);
            proveedorService.crearProveedor(proveedor);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(mapper.writeValueAsString(proveedor));
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
            String pathInfo = req.getPathInfo();
            Long id = Long.parseLong(pathInfo.substring(1));
            Proveedor proveedor = mapper.readValue(req.getInputStream(), Proveedor.class);
            proveedor.setIdProveedor(id);
            Proveedor actualizado = proveedorService.actualizarProveedor(proveedor);
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
            String pathInfo = req.getPathInfo();
            Long id = Long.parseLong(pathInfo.substring(1));
            proveedorService.eliminarProveedor(id);
            resp.getWriter().write("{\"mensaje\":\"Proveedor eliminado correctamente\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}