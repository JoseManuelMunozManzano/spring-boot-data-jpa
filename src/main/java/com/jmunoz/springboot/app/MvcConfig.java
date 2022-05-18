package com.jmunoz.springboot.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // Para agregar directorios/recursos a nuestro proyecto
    // Es un modelo de WebMvcConfigurer
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        // El resourceHandler es lo que aparece en ver.html (/uploads)
        // El ** es para mapear al nombre de la imagen y que se pueda cargar
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/Users/jmmm/Programacion/JAVA/Spring/Udemy-SpringFramework5YSpringBoot2_AndresJoseGuzman/00-Desarrollos/uploads/");

    }
}
