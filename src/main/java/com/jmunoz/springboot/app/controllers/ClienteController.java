package com.jmunoz.springboot.app.controllers;

import com.jmunoz.springboot.app.models.dao.IClienteDao;
import com.jmunoz.springboot.app.models.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    // Primera fase: Mostrar el formulario al usuario
    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model) {
        // Cliente es un objeto de negocio bidireccional. No solo está mapeado a la tabla, también al formulario.
        Cliente cliente = new Cliente();

        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Clientes");
        return "form";
    }

    // Segunda fase: el usuario envía en el submit los datos del formulario (los datos de cliente poblados) y los guardamos
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(Cliente cliente) {
        clienteDao.save(cliente);

        return "redirect:listar";
    }
}
