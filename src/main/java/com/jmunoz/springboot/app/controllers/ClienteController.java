package com.jmunoz.springboot.app.controllers;

import com.jmunoz.springboot.app.models.dao.IClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
}
