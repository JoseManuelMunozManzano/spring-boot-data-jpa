package com.jmunoz.springboot.app.controllers;

import com.jmunoz.springboot.app.models.service.IClienteService;
import com.jmunoz.springboot.app.view.xml.ClienteList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador específico dedicado a un API Rest
// En vez de usar la anotación @Controller se usa la anotación @RestController, que combina 2 anotaciones,
// @Controller y @ResponseBody. Por lo tanto, to-dos sus métodos van a ser @ResponseBody
// Para probar
//  XML:   http://localhost:8080/api/clientes/listar
//  JSON:  http://localhost:8080/api/clientes/listar?format=json

@RestController
@RequestMapping("/api/clientes")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    // Ya no hace falta anotar con @ResponseBody porque ya la maneja @RestController
    @GetMapping(value = "/listar")
    public ClienteList listar() {

        return new ClienteList(clienteService.findAll());
    }
}
