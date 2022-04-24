package com.jmunoz.springboot.app.models.dao;

import com.jmunoz.springboot.app.models.entity.Cliente;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

// Se indica el nombre para especificar en el controlador la versión concreta que se quiere inyectar
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
}
