package com.jmunoz.springboot.app.models.service;

import com.jmunoz.springboot.app.models.dao.IClienteDao;
import com.jmunoz.springboot.app.models.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Una clase service está basada en el patrón de diseño Facade.
// Es un único punto de acceso a distintos DAOs o repository.
// En el ejemplo hay un DAO, pero podría haber muchos y dentro de un método se podría interactuar con distintos DAO.
// Se evita acceder directamente a los DAO desde los controladores.
// También se puede manejar la transacción sin tener que implementar las anotaciones @Transactional en el DAO.
// Nos hemos traído la anotación @Transactional de la clase ClienteDaoImpl aquí.
@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteDao clienteDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return clienteDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findOne(Long id) {
        return clienteDao.findOne(id);
    }

    @Override
    @Transactional
    public void save(Cliente cliente) {
        clienteDao.save(cliente);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clienteDao.delete(id);
    }
}
