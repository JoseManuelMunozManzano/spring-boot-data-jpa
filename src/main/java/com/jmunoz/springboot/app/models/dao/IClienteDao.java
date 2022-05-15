package com.jmunoz.springboot.app.models.dao;

import com.jmunoz.springboot.app.models.entity.Cliente;
import org.springframework.data.repository.PagingAndSortingRepository;

// Para añadir Paginación hay que heredar de PagingAndSortingRepository en vez de CrudRepository
// Pero hereda de CrudRepository, por lo que obtenemos todas las funcionalidades del CRUD junto a la Paginación.
// En vez de Iterable, el método findAll devuelve un Page, pero por detrás implementa la interface Iterable.
//
// Ver IClienteService y ClienteServiceImpl para ver como manejar la paginación
public interface IClienteDao extends PagingAndSortingRepository<Cliente, Long> {

}
