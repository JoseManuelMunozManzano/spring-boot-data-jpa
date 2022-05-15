package com.jmunoz.springboot.app.controllers;

import com.jmunoz.springboot.app.models.entity.Cliente;
import com.jmunoz.springboot.app.models.service.IClienteService;
import com.jmunoz.springboot.app.util.paginator.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    // Uso de paginación
    // Se obtiene la página actual a través de la ruta URL (un @RequestParam)
    // El defaultValue es importante que sea 0 para que la primera página empiece en 0
    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        // Hay 2 formas de crear un Pageable
        // La forma antigua usando el operador new.
        // Esta forma esta deprecated. Viene de la versión SpringBoot 1.5
//        Pageable pageRequest = new PageRequest(page, 4);
        // La forma nueva, desde SpringBoot 2, usa la creación estática sin el operador new, usando el método estático of
        // Se le pasan dos enteros, indicando la página actual y el número de elementos a mostrar por página.
        Pageable pageRequest = PageRequest.of(page, 4);

        // Obtenemos la lista paginable
        Page<Cliente> clientes = clienteService.findAll(pageRequest);
        // Se la pasamos a la clase que calcula los elementos que se pasa a la vista
        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);
        // Pasamos los elementos a la vista
        model.addAttribute("page", pageRender);

        return "listar";
    }

    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model) {
        Cliente cliente = new Cliente();

        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Cliente");
        return "form";
    }

    // RedirectAttributes nos sirve para añadir los mensajes flash
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }

        String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito!" : "Cliente creado con éxito!";

        clienteService.save(cliente);
        status.setComplete();
        // Usamos el método addFlashAttribute para mandar el mensaje flash
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:listar";
    }

    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
        Cliente cliente = null;

        if (id > 0 ){
            cliente = clienteService.findOne(id);
            if (cliente == null) {
                flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD!");
                return "redirect:/listar";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del cliente no puede ser cero!");
            return "redirect:/listar";
        }

        model.put("cliente", cliente);
        model.put("titulo", "Editar Cliente");
        return "form";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {

        if (id > 0) {
            clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con éxito!");
        }

        return "redirect:/listar";
    }
}
