package com.jmunoz.springboot.app.models.service;

import com.jmunoz.springboot.app.models.dao.IClienteDao;
import com.jmunoz.springboot.app.models.dao.IFacturaDao;
import com.jmunoz.springboot.app.models.dao.IProductoDao;
import com.jmunoz.springboot.app.models.entity.Cliente;
import com.jmunoz.springboot.app.models.entity.Factura;
import com.jmunoz.springboot.app.models.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements IClienteService {

    @Autowired
    private IClienteDao clienteDao;

    @Autowired
    private IProductoDao productoDao;

    @Autowired
    private IFacturaDao facturaDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        // CrudRepository devuelve un Iterable, no un List<>
        return (List<Cliente>) clienteDao.findAll();
    }

    // Paginación. Se devuelve un Page, que es un subconjunto de Iterable
    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteDao.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findOne(Long id) {
        // findOne no existe en CrudRepository. Se llama findById
        // Retorna un Optional
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente fetchByIdWithFacturas(Long id) {
        return clienteDao.fetchByIdWithFacturas(id);
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

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findByNombre(String term) {
        return productoDao.findByNombreLikeIgnoreCase("%" + term + "%");
    }

    @Override
    @Transactional
    public void saveFactura(Factura factura) {
        facturaDao.save(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findProductoById(Long id) {
        return productoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Factura findFacturaById(Long id) {
        return facturaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteFactura(Long id) {
        facturaDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id) {
        return facturaDao.fetchByIdWithClienteWithItemFacturaWithProducto(id);
    }
}
