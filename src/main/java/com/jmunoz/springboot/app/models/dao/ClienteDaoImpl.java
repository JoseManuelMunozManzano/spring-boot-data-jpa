package com.jmunoz.springboot.app.models.dao;

import com.jmunoz.springboot.app.models.entity.Cliente;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository("clienteDaoJPA")
public class ClienteDaoImpl implements IClienteDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        // Para evitar type safety unchecked y tener que poner suppress warning
        TypedQuery<Cliente> query = em.createQuery("from Cliente", Cliente.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void save(Cliente cliente) {
        // persist() almacena un objeto entity en el contexto de persistencia (JPA) y en la base de datos.
        // Una vez se realice el commit() y el flush() va a sincronizar con la BD y va a realizar el insert
        // en la tabla. To-do esto se hace de forma automática.
        em.persist(cliente);
    }
}
