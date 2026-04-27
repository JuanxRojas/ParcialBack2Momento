package com.compensar.inventario.dao;

import com.compensar.inventario.model.entity.Cliente;

public class ClienteDAO extends GenericDAO<Cliente> {

    public ClienteDAO() {
        super(Cliente.class);
    }
}