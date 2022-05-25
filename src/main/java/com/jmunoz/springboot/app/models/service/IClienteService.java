package com.jmunoz.springboot.app.models.service;

import com.jmunoz.springboot.app.models.entity.Cliente;
import com.jmunoz.springboot.app.models.entity.Factura;
import com.jmunoz.springboot.app.models.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService {
    List<Cliente> findAll();

    // Para usar Paginación. Ver también IClienteDao
    Page<Cliente> findAll(Pageable pageable);

    void save(Cliente cliente);

    Cliente findOne(Long id);

    void delete(Long id);

    List<Producto> findByNombre(String term);

    void saveFactura(Factura factura);

    Producto findProductoById(Long id);
}
