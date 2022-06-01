package com.jmunoz.springboot.app.models.service;

import com.jmunoz.springboot.app.models.dao.IUsuarioDao;
import com.jmunoz.springboot.app.models.entity.Role;
import com.jmunoz.springboot.app.models.entity.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// Para este service no necesitamos implementar una interface, ya que la interface la provee SpringSecurity.
// Es una interface propia de SpringSecurity para trabajar con JPA o cualquier otro tipo de proveedor para
// implementar el proceso de autenticación.
@Service("jpaUserDetailsService")
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private IUsuarioDao usuarioDao;

    private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);

    // UserDetails es una interface de SpringSecurity que representa un usuario autenticado,
    // pero se tiene que retornar la implementación (User)
    //
    // No se nos olvide decorar con @Transactional ya que bajo la misma transacción vamos a realizar la consulta
    // del usuario y a obtener los roles.
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findByUsername(username);

        // Se añade logger y manejo de errores
        if (usuario == null) {
            logger.error("Error login: no existe el usuario '" + username + "'");
            throw new UsernameNotFoundException("Username " + username + " no existe en el sistema!");
        }

        // Obtenemos los roles (era carga LAZY) y los registramos en una lista GrantedAuthority
        // GrantedAuthority es el tipo abstracto y la implementa SimpleGrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role: usuario.getRoles()) {
            logger.info("Role: ".concat(role.getAuthority()));
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        if (authorities.isEmpty()) {
            logger.error("Error login: usuario '" + username + "' no tiene roles asignados!");
            throw new UsernameNotFoundException("Error login: usuario '" + username + "' no tiene roles asignados!");
        }

        // La comparación de la contraseña ingresada en el formulario encriptada en bcrypt
        // con la contraseña del objeto User que retornamos en el método loadUserByUsername
        // lo realiza el framework automáticamente por debajo. No se ve.
        return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
    }
}
