package com.jmunoz.springboot.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // Manejo de errores de acceso denegado
    //
    // Método para implementar un controlador de vista (ViewController)
    // El método tiene que llamarse addViewControllers obligatoriamente.
    // Recibe como argumento un ViewControllerRegistry para registrar el ViewController.
    // Serían controladores parametrizables o estáticos. Simplemente, cargan la vista y tienen una URL,
    // lo que sería el @GetMapping, y se indica la vista que se asocia a esa ruta.
    // Pero no tienen lógica del controlador.
    //
    // NOTA: En vez de esto, también se podría haber hecho una clase controlador, anotada con @Controller, con una url
    // y un método que hiciese un return a una vista, sin ningún tipo de lógica.
    // Pero esto es más sencillo.
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error_403").setViewName("error_403");
    }
}
