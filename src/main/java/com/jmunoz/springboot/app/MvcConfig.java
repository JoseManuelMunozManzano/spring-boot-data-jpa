package com.jmunoz.springboot.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

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

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("es", "ES"));
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("lang");
        return localeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    // Conversor que convierte un objeto en un XML (Marshalling)
    // Se va a utilizar en nuestra vista XML para poder convertir nuestra respuesta (el objeto Entity) en un
    // documento XML.
    // XML no puede convertir un objeto Collection (como un List) en XML. Necesitamos una clase wrapper que envuelva
    // nuestra lista.
    // Si, por ejemplo, queremos convertir nuestro listado de clientes, necesitamos una clase ClienteList que envuelva
    // el objeto List o Collection dentro de esta clase.
    // Esta clase va a representar el XML root, la raiz del documento XML.
    //
    // Tanto la clave wrapper como la clave vista están en el package view/xml
    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // Las clases que se van a convertir en XML (la clase wrapper)
        marshaller.setClassesToBeBound(new Class[] {com.jmunoz.springboot.app.view.xml.CilenteList.class});
        return marshaller;
    }
}
