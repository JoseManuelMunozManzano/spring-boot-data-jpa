package com.jmunoz.springboot.app;

import com.jmunoz.springboot.app.auth.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccessHandler successHandler;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Inyectamos el dataSource para la conexión a la BD
    @Autowired
    private DataSource dataSource;

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
        // Ya no usamos la autenticación en memoria

//        PasswordEncoder encoder = passwordEncoder;
//
//        UserBuilder users = User.builder().passwordEncoder(encoder::encode);
//
//        builder.inMemoryAuthentication()
//                .withUser(users.username("admin").password("1234").roles("ADMIN", "USER"))
//                .withUser(users.username("jmunoz").password("1234").roles("USER"));

        // Vamos a configurar e implementar SpringSecurity utilizando JDCB con MySql
        // A través del objeto builder hacemos la autenticación:
        //  1. Configuramos el dataSource, pasando como instancia la conexión que tenemos inyectada
        //  2. Pasar el passwordEncoder
        //  3. Armar la consulta sql para los usuarios. Podemos modificar nuestra consulta de inicio de
        //     sesión. En este caso sería una consulta SQL nativa, nada que ver con JPA (después se hará con JPA)
        //     Es muy sencilla
        //  4. Armar la consulta sql para los roles
        builder.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("select username, password, enabled " +
                        "from users " +
                        "where username = ?")
                .authoritiesByUsernameQuery("select u.username, a.authority " +
                        "from authorities a " +
                        "inner join users u on (a.user_id = u.id) " +
                        "where u.username = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                        .successHandler(successHandler)
                        .loginPage("/login")
                    .permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error_403");
    }
}
