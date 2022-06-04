package com.jmunoz.springboot.app.view.xml;

import com.jmunoz.springboot.app.models.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.xml.MarshallingView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

// Queremos exportar el listado de clientes
@Component("listar.xml")
public class ClienteListXmlView extends MarshallingView {

    // Se inyecta en el constructor el bean que creamos en MvcConfig
    @Autowired
    public ClienteListXmlView(Jaxb2Marshaller marshaller) {
        super(marshaller);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // El método listar de ClienteController añade al model título, page y clientes.
        // Tenemos que borrarlos, pero clientes, antes de borrarlo, tenemos que obtenerlos para hacer la conversión
        model.remove("titulo");
        model.remove("page");

        Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
        model.remove("clientes");

        // Con el model ya limpio, guardamos el wrapper ClienteList con el listado de clientes.
        // Notar que este ejemplo se hace con los clientes paginados, no con todos.
        // Si queremos todos, tenemos que hacer una consulta para obtener todos los clientes.
        model.put("clienteList", new ClienteList(clientes.getContent()));

        super.renderMergedOutputModel(model, request, response);
    }
}
