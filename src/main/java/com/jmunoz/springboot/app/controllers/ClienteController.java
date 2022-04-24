package com.jmunoz.springboot.app.controllers;

import com.jmunoz.springboot.app.models.dao.IClienteDao;
import com.jmunoz.springboot.app.models.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class ClienteController {

    // Aunque no haría falta porque solo hay 1, se indica el bean concreto que queremos instanciar
    @Autowired
    @Qualifier("clienteDaoJPA")
    private IClienteDao clienteDao;

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String listar(Model model) {
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clienteDao.findAll());

        return "listar";
    }

    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model) {
        Cliente cliente = new Cliente();

        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Clientes");
        return "form";
    }

    // Con BindingResult se muestran en la vista los mensajes de error de las validaciones
    // Siempre se ubica junto al objeto del formulario. Los dos van siempre juntos
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario de Clientes");
            // El cliente se pasa de forma automática cuando el tipo de la clase (Cliente) se llame igual
            // que el nombre con el cual lo estamos pasando a la vista (cliente)
            // No se tiene en cuenta que el tipo de la clase empieza en mayúsculas y el atributo to-do en minúsculas.
            // Si se llamase distinto habría que informar el parámetro arriba de la siguiente forma:
            // @Valid @ModelAttribute("el_nombre") Cliente cliente
            return "form";
        }

        clienteDao.save(cliente);
        return "redirect:listar";
    }
}
