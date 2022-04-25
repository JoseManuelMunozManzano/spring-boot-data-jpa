package com.jmunoz.springboot.app.models.dao;

import com.jmunoz.springboot.app.models.entity.Cliente;

import java.util.List;

public interface IClienteDao {
    List<Cliente> findAll();

    void save(Cliente cliente);

    Cliente findOne(Long id);
}
