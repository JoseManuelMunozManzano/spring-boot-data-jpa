package com.jmunoz.springboot.app.models.dao;

import com.jmunoz.springboot.app.models.entity.Factura;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IFacturaDao extends CrudRepository<Factura, Long> {

    // Ahora mismo se lanzan 3 consultas (facturas, clientes, item_facturas) y 1 consulta
    // por producto en la tabla Producto.
    // Se optimiza usando JOIN FETCH, para obtener la factura con todas las relaciones hechas de una vez,
    // sin tener que realizar consultas después en la vista de forma perezosa (LAZY)
    // Solo una consulta.
    // Esta es la forma recomendada cuando se trabaja con un objeto Entity que tiene muchas relaciones.
    //
    // join fetch f.cliente porque si revisamos el entity Factura tiene un atributo de relación llamado cliente.
    // Va a obtener un cliente.
    // Igual en join fetch f.items. Aquí va a obtener muchas líneas.
    // El atributo producto está relacionado con ItemFactura, de ahí que se indique join fetch l.producto.
    // Por cada línea recupera su producto.
    @Query("select f from Factura f join fetch f.cliente c join fetch f.items l join fetch l.producto where f.id = ?1")
    public Factura fetchByIdWithClienteWithItemFacturaWithProducto(Long id);
}
