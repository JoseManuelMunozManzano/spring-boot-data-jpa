package com.jmunoz.springboot.app.controllers;

import com.jmunoz.springboot.app.models.entity.Cliente;
import com.jmunoz.springboot.app.models.entity.Factura;
import com.jmunoz.springboot.app.models.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

    @Autowired
    private IClienteService clienteService;

    // Método para desplegar el formulario en la vista
    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId,
                        Map<String, Object> model,
                        RedirectAttributes flash) {

        Cliente cliente = clienteService.findOne(clienteId);
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }

        // Se crea la factura y se relaciona con el cliente
        // Es muy importante mantener el objeto factura en una session hasta que se procesa el formulario y se envía
        // al método guardar que persiste la factura en la BBDD junto con el cliente y sus items.
        //
        // Ver en ClienteController, en el método guardar como existe el parámetro SessionStatus y cuando se graba
        // el cliente se hace un status.setComplete() y se elimina de la sesión.
        // Aquí se hará igual.
        //
        // Por eso se indica el @SessionAttributes con este nombre factura en la cabecera de la clase
        Factura factura = new Factura();
        factura.setCliente(cliente);

        model.put("factura", factura);
        model.put("titulo", "Crear Factura");

        return "factura/form";
    }
}
