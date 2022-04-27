package com.jmunoz.springboot.app.models.service;

import com.jmunoz.springboot.app.models.dao.IClienteDao;
import com.jmunoz.springboot.app.models.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteDao clienteDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        // CrudRepository devuelve un Iterable, no un List<>
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findOne(Long id) {
        // findOne no existe en CrudRepository. Se llama findById
        // Retorna un Optional
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void save(Cliente cliente) {
        clienteDao.save(cliente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // delete no existe en CrudRepository. Se llama deleteById
        clienteDao.deleteById(id);
    }
}
