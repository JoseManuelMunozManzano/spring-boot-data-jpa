package com.jmunoz.springboot.app.models.dao;

import com.jmunoz.springboot.app.models.entity.Cliente;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

// Nos llevamos las anotaciones @Transactional a ClienteServiceImpl
@Repository("clienteDaoJPA")
public class ClienteDaoImpl implements IClienteDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Cliente> findAll() {
        // Para evitar type safety unchecked y tener que poner suppress warning
        TypedQuery<Cliente> query = em.createQuery("from Cliente", Cliente.class);
        return query.getResultList();
    }

    @Override
    public Cliente findOne(Long id) {
        return em.find(Cliente.class, id);
    }

    @Override
    public void save(Cliente cliente) {
        if (cliente.getId() != null && cliente.getId() > 0) {
            em.merge(cliente);
        } else {
            em.persist(cliente);
        }
    }

    @Override
    public void delete(Long id) {
        // Este id viene de los atributos de la sesión
        em.remove(findOne(id));
    }
}
