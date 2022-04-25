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
        // Este id viene del campo hidden de form.html. Para editar estará informado y para alta estará a null
        if (cliente.getId() != null && cliente.getId() > 0) {
            // Con merge se actualizan los datos existentes
            em.merge(cliente);
        } else {
            em.persist(cliente);
        }
    }

    @Override
    public Cliente findOne(Long id) {
        // Se usa el método find del entity manager (JPA). Se le pasa la clase y el id.
        // De forma automática, JPA va a la BD y nos entrega el objeto cliente que tiene ese id.
        // No olvidar que queda manejado dentro del contexto de persistencia.
        return em.find(Cliente.class, id);
    }
}
