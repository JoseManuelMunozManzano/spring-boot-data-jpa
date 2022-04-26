package com.jmunoz.springboot.app.models.service;

import com.jmunoz.springboot.app.models.entity.Cliente;

import java.util.List;

public interface IClienteService {
    List<Cliente> findAll();

    void save(Cliente cliente);

    Cliente findOne(Long id);

    void delete(Long id);
}
