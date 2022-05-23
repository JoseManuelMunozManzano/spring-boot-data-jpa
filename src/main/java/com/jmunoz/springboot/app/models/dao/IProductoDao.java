package com.jmunoz.springboot.app.models.dao;

import com.jmunoz.springboot.app.models.entity.Producto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IProductoDao extends CrudRepository<Producto, Long> {

    // Primera forma de implementar un like
    // Usando @Query e implementando la consulta con un select a nivel de objeto Entity, no a nivel de tabla.
    // ?1 indica el primer parámetro, en este caso term
    @Query("select p from Producto p where p.nombre like %?1%")
    List<Producto> findByNombre(String term);

    // Segunda forma de implementar un like
    // Usando una consulta basada en el nombre del método
    // Automáticamente se hace la consulta gracias a que el nombrado del método sigue un standard
    List<Producto> findByNombreLikeIgnoreCase(String term);
}
