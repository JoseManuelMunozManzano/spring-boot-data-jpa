package com.jmunoz.springboot.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

// Nuestra personalización de la página Login
@Controller
public class LoginController {

    // Se añade la gestión del error. El error viene en el request.
    // No es requerido porque puede que no venga ningún error.
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
            Model model, Principal principal, RedirectAttributes flash) {

        if (principal != null) {
            flash.addFlashAttribute("info", "Ya ha iniciado sesión anteriormente");
            return "redirect:/";
        }

        if (error != null) {
            model.addAttribute("error",
                    "Error en el login: Nombre de usuario o contraseña incorrecta. " +
                            "Por favor, vuelva a intentarlo!");
        }

        return "login";
    }
}
