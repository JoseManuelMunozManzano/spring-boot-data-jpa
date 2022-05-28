package com.jmunoz.springboot.app.models.dao;

import com.jmunoz.springboot.app.models.entity.Cliente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {

    // JOIN FETCH para obtener, sin carga perezosa, todas las facturas del cliente.
    // Ahora mismo hace 2 consultas para recuperar esa información y queremos que sea solo una.
    //
    // join fetch se traduce luego en inner join así que podemos tener un problema si
    // el cliente no tiene facturas, ya que no va a recuperar ninguna información,
    // ni siquiera del cliente, y se verá en la vista que el cliente no existe en la base de datos,
    // cuando realmente si existe y no tiene facturas asociadas.
    //
    // Es por eso que se puede ver left join fetch para que trate aparte la relación de la izquierda, el cliente.
    // left join fetch se traduce en left outer join
    @Query("select c from Cliente c left join fetch c.facturas f where c.id = ?1")
    public Cliente fetchByIdWithFacturas(Long id);

}
