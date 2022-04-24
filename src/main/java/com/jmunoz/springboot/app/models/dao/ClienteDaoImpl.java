package com.jmunoz.springboot.app.models.dao;

import com.jmunoz.springboot.app.models.entity.Cliente;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

// @Repository es una anotación de Spring para marcar la clase como componente de persistencia, de acceso a datos.
// Internamente es un estereotipo de @Component.
// Además se encarga de traducir las excepciones que puedan ocurrir, como DataAccessException
@Repository
public class ClienteDaoImpl implements IClienteDao {

    // EntityManager se encarga de manejar las clases de entidades.
    // El ciclo de vida las persiste dentro del contexto, las actualiza, las elimina, puede realizar consultas...
    // es decir, todas las operaciones a la BD pero a nivel de objeto, a través de las clases Entity.
    // Por lo tanto las consultas son siempre de JPA. Van a la clase Entity, no a la tabla.
    //
    // @PersistenceContext
    // De forma automática inyecta el EntityManager según la configuración de la unidad de persistencia que contiene
    // el DataSource, el proveedor JPA. Si no se configura ningún tipo de motor en application.properties, por defecto
    // Spring Boot usa la BD H2 embebida en la aplicación.
    // Por ahora se va a usar la configuración por defecto, H2, y después se va a trabajar con MySql.
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        // Para evitar type safety unchecked y tener que poner suppress warning
        TypedQuery<Cliente> query = em.createQuery("from Cliente", Cliente.class);
        return query.getResultList();
    }
}
