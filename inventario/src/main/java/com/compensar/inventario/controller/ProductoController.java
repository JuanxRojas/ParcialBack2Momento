package com.compensar.inventario.controller;

import com.compensar.inventario.model.entity.Producto;
import com.compensar.inventario.model.entity.Proveedor;
import com.compensar.inventario.service.ProductoService;
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

@WebServlet("/api/productos/*")
public class ProductoController extends HttpServlet {

    private ProductoService productoService = new ProductoService();
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
                List<Producto> lista = productoService.listarProductos();
                resp.getWriter().write(mapper.writeValueAsString(lista));
            } else {
                Long id = Long.parseLong(pathInfo.substring(1));
                Producto producto = productoService.obtenerProducto(id);
                resp.getWriter().write(mapper.writeValueAsString(producto));
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
            Map<String, Object> body = mapper.readValue(req.getInputStream(), Map.class);
            Producto producto = new Producto();
            producto.setNombre((String) body.get("nombre"));
            producto.setDescripcion((String) body.get("descripcion"));
            producto.setPrecio(new java.math.BigDecimal(body.get("precio").toString()));
            producto.setStock((Integer) body.get("stock"));

            Proveedor proveedor = new Proveedor();
            proveedor.setIdProveedor(Long.parseLong(body.get("idProveedor").toString()));
            producto.setProveedor(proveedor);

            productoService.crearProducto(producto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(mapper.writeValueAsString(producto));
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
            Map<String, Object> body = mapper.readValue(req.getInputStream(), Map.class);

            Producto producto = productoService.obtenerProducto(id);
            if (body.containsKey("nombre"))      producto.setNombre((String) body.get("nombre"));
            if (body.containsKey("descripcion")) producto.setDescripcion((String) body.get("descripcion"));
            if (body.containsKey("precio"))      producto.setPrecio(new java.math.BigDecimal(body.get("precio").toString()));
            if (body.containsKey("stock"))       producto.setStock((Integer) body.get("stock"));

            Producto actualizado = productoService.actualizarProducto(producto);
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
            productoService.eliminarProducto(id);
            resp.getWriter().write("{\"mensaje\":\"Producto eliminado correctamente\"}");
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}