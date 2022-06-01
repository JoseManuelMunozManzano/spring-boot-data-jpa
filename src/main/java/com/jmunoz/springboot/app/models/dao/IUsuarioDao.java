package com.jmunoz.springboot.app.models.dao;

import com.jmunoz.springboot.app.models.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

// Consulta JPA para Login a través del username.
// Los roles los obtenemos automáticamente a través de la relación oneToMany.
// Por tanto, solo hace falta una consulta.
//
// Esto reemplaza lo que hicimos con JDBC en la clase SpringSecurityConfig.java, método configurerGlobal
public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

    // A través del nombre del método (Query method name) se ejecutará la consulta JPQL siguiente:
    // "select u from Usuario u where u.username = ?1"
    public Usuario findByUsername(String username);

    // Otra forma sería crear un método con cualquier nombre y poner la anotación @Query con la consulta JPA
    // Por ejemplo, lo mismo de arriba sería con @Query:
//    @Query("select u from Usuario where u.username=?1")
//    public Usuario fetchByUsername(String username);
}
