package com.jmunoz.springboot.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

// Nuestra personalización de la página Login
@Controller
public class LoginController {

    // Esta es la página de login personalizada.
    // Si to-do sale bien el usuario queda logeado.
    // Si algo sale mal, tipo el usuario no existe o clave incorrecta, el mensaje de error también se
    // puede personalizar en esta misma acción, ya que por defecto va a redirigir hacia este controlador
    // con la url login, el texto del error y el problema que ocurrió.
    // Esto se va a personalizar después.
    //
    // El objeto principal es muy importante. Contiene el usuario logueado y permite validar.
    // Si el usuario ya ha iniciado sesión redirigimos a la página de inicio y no volvemos a mostrar
    // la página de login, evitando un doble inicio de sesión.
    //
    // No olvidar habilitar la página en la configuración SpringSecurityConfig con la ruta indicada en el @GetMapping
    @GetMapping("/login")
    public String login(Model model, Principal principal, RedirectAttributes flash) {

        if (principal != null) {
            flash.addFlashAttribute("info", "Ya ha iniciado sesión anteriormente");
            return "redirect:/";
        }

        return "login";
    }
}
