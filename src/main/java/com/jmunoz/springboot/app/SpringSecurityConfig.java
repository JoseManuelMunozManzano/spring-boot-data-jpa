package com.jmunoz.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Clase de Configuración para REGISTRAR USUARIOS
// Se extiende de WebSecurityConfigurerAdapter y se tiene que implementar un método para poder registrar y
// configurar los usuarios de nuestro sistema de seguridad (usuario, password y roles)
// Partimos de lo simple, es decir, guardando estos usuarios en memoria y después se guardarán en BD con JDBC y JPA.
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    // Registrar nuestro PasswordEncoder
    //
    // Ahora mismo, con SpringSecurity5, solo queda sin estar deprecado el encoder bcrypt.
    // Si se quiere usar otro PasswordEncoder distinto a bcrypt, lo mejor es usar una versión de
    // SpringBoot anterior, por ejemplo la 1.5.x que trabaja con SpringSecurity4.
    // Pero por temas de seguridad se recomienda usar bcrypt.
    //
    // Para migrar a SpringSecurity5 hay que dar una implementación de PasswordEncoder usando bcrypt.
    // Creamos la instancia, la retornamos y la guardamos en el contenedor (gracias a @Bean) y es la que va a usar
    // por defecto SpringSecurity
    //
    // Importante el static para que no de error al crear el Bean.
    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Se inyecta el componente AuthenticationManagerBuilder, que sirve para registrar los usuarios
    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
        // Este es el passwordEncoder retornado en el @Bean de arriba.
        PasswordEncoder encoder = passwordEncoder();

        // Ahora con el encoder podemos crear los usuarios y encriptar su contraseña.
        // A passwordEncoder se le pasa una función de flecha (expresión lambda). Recibe el password y
        // por cada usuario que registremos automáticamente se genera un evento que va a encriptar la contraseña,
        // cada vez que invoquemos el método password() para generar la contraseña del usuario.
        // UserBuilder users = User.builder().passwordEncoder(password -> encoder.encode(password));
        UserBuilder users = User.builder().passwordEncoder(encoder::encode);

        // Ahora podemos crear los usuarios. Por ahora en memoria.
        // Contiene el username, password y roles.
        // Recordar que automáticamente, al llamar a password(), el userBuilder va a codificar la clave.
        // En este caso el usuario admin tiene dos roles: ADMIN y USER
        // Y se crea otro usuario.
        builder.inMemoryAuthentication()
                .withUser(users.username("admin").password("1234").roles("ADMIN", "USER"))
                .withUser(users.username("jmunoz").password("1234").roles("USER"));
    }

    // CONFIGURACION AUTORIZACIONES HTTP, para dar seguridad a todas nuestras páginas
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Aquí asignamos nuestras rutas
        // Primero vamos a poner las rutas públicas a usuarios registrados y anónimos. Se usa permitAll()
        // Y luego ponemos las rutas privadas, las que queremos proteger. En este caso se indica el rol también
        // y se termina con anyRequest().authenticated()
        //
        // Cuando configuramos nuestras rutas y damos autorización, por detrás Spring Security ejecuta un
        // interceptor antes de cargar cualquier ruta de nuestros controladores y evalúa que nuestro usuario tenga
        // permiso. Si no tiene permiso mostrará una página de error indicando acceso denegado.
        // Si tenemos configurado nuestra página de login, en vez de mostrar la página acceso denegado, va a
        // redirigir a la página de login.
        // Por defecto, la ruta, el @RequestMapping es login
        http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
                .antMatchers("/ver/**").hasAnyRole("USER")
                .antMatchers("/uploads/**").hasAnyRole("USER")
                .antMatchers("/form/**").hasAnyRole("ADMIN")
                .antMatchers("/eliminar/**").hasAnyRole("ADMIN")
                .antMatchers("/factura/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated();
    }
}
