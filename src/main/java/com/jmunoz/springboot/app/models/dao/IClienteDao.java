package com.jmunoz.springboot.app.models.dao;

import com.jmunoz.springboot.app.models.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

// Para documentación de CrudRepository de JPA ver:
// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
// Se hace uso de la interface CrudRepository, que ya tiene por defecto un CRUD interno
// y no hace falta declarar los métodos
// En el Generic hay que informar la clase Entity y el tipo de dato de nuestro campo id
//
// Notar que no hay anotación. Es porque CrudRepository es una interface especial.
// Por debajo ya es un componente Spring y no hay necesidad de indicarlo con una anotación
// Pulsar Cmd+click en CrudRepository para ver los métodos que tiene integrados y que devuelven.
public interface IClienteDao extends CrudRepository<Cliente, Long> {

    // CrudRepository ya viene con métodos por defecto, pero se pueden añadir otros con consultas
    // personalizadas, con @Query o con lo method names.
}
