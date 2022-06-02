package com.jmunoz.springboot.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

// Vamos a solucionar el problema de perder parámetros de la url en una petición GET
// (ejemplo del paginador que se perdía en la url cuando se pulsaba un link de idioma)
// Este controlador se encarga de obtener del request la cabecera (header) y a través de él vamos a obtener
// el link de la última URL en la que estábamos, que sería el parámetro referer.
// Vamos a obtener ese último link y vamos a redirigir a la última url en la que estábamos.
//
// También en layout.html hay que pasar la ruta a este controlador, no solo el parámetro a secas
@Controller
public class LocaleController {

    @GetMapping("/locale")
    public String locale(HttpServletRequest request) {
        String ultimaUrl = request.getHeader("referer");

        return "redirect:".concat(ultimaUrl);
    }
}
