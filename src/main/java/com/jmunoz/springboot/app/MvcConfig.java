package com.jmunoz.springboot.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error_403").setViewName("error_403");
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración para implementar el multilenguaje (locale)
    // 1. Se implementa el bean LocaleResolver que es una interface que se encarga del adaptador en el cual vamos a
    //      guardar nuestro locale con nuestra internacionalización.
    //      Por ejemplo, puede ser en una sesión http, en una cookie...
    //      Es decir, donde se va a guardar el parámetro de nuestro lenguaje (locale).
    // 2. También se implementa el bean LocaleChangeInterceptor que se encarga de modificar el locale cada vez
    //      que se envía el parámetro del lenguaje con el nuevo idioma.
    //      Un interceptor se ejecuta en cada request y se encarga de realizar alguna tarea justo antes de invocar un
    //      método handler del controlador (como un middleware de NodeJs)
    // 3. Finalmente, tenemos que implementar un método para registrar este interceptor.

    @Bean
    public LocaleResolver localeResolver() {
        // En este caso se guarda en una sesión http (sessionLocalResolver)
        // SessionLocaleResolver localeResolver = new SessionLocaleResolver();

        // Ejemplo guardando en Cookie el Locale
        // Con esto se evita que, si estoy en el idioma Inglés, cuando pulso LogOut, como se pierde la sesión
        // el idioma vuelva al por defecto (español)
        // Aunque se pierda la sesión, como ya tengo el idioma en una Cookie, no se pierde el idioma.
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();

        // Se indica código del locale y el país
        localeResolver.setDefaultLocale(new Locale("es", "ES"));
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        // Cada vez que se pase por url a través del método GET el parámetro lang, por ejemplo con el valor "es_ES"
        // se va a ejecutar este interceptor y va a realizar el cambio de lenguaje.
        localeInterceptor.setParamName("lang");
        return localeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
