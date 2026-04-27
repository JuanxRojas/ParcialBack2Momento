package com.compensar.inventario.service;

import com.compensar.inventario.dao.ClienteDAO;
import com.compensar.inventario.model.entity.Cliente;
import java.util.List;

public class ClienteService {

    private ClienteDAO clienteDAO = new ClienteDAO();

    public void crearCliente(Cliente cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            throw new RuntimeException("El nombre del cliente es obligatorio");
        }
        clienteDAO.save(cliente);
    }

    public Cliente obtenerCliente(Long id) {
        Cliente cliente = clienteDAO.findById(id);
        if (cliente == null) {
            throw new RuntimeException("Cliente no encontrado con id: " + id);
        }
        return cliente;
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.findAll();
    }

    public Cliente actualizarCliente(Cliente cliente) {
        if (cliente.getIdCliente() == null) {
            throw new RuntimeException("El id del cliente es obligatorio para actualizar");
        }
        return clienteDAO.update(cliente);
    }

    public void eliminarCliente(Long id) {
        clienteDAO.delete(id);
    }
}