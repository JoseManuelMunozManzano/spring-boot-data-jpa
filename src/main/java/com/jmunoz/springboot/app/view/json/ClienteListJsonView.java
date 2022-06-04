package com.jmunoz.springboot.app.view.json;

// En SpringBoot ya se incluye por defecto la dependencia para trabajar con JSON en spring-boot-starter-web.
// Es parte natural del framework, ya que una de las cosas típicas es implementar servicios o API Rest.
//
// No hay que realizar, por tanto, ninguna configuración ni en el POM, ni hay que configurar ningún Bean en MvcConfig.
// Tampoco necesitamos una clase wrapper, porque serializa perfectamente los objetos de colección
//
// Solo tenemos que crear esta vista JSON

import com.jmunoz.springboot.app.models.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Map;

@Component("listar.json")
public class ClienteListJsonView extends MappingJackson2JsonView {

    // Implementamos este método para quitar algunos elementos del model que pasamos a la vista y que no nos interesan
    @Override
    protected Object filterModel(Map<String, Object> model) {
        // Tal y como hacíamos en la conversión a XML, no queremos ni el título ni page
        model.remove("titulo");
        model.remove("page");

        // Si ejecutamos ya rota la bidireccionalidad (ver entity Cliente) todavía aparecen datos que no nos
        // interesan, por ejemplo content (esto es lo que queremos), pageable, totalPages, sort, numberOfElements...
        // Hay que filtrar parecido a lo que se hizo en XML, que se pasó el cliente a una clase wrapper,
        // se eliminó y se obtuvo el listado de clientes.
        // En este caso la clase wrapper no es necesaria. Solo hace falta el listado, el clientes.getContent()
        Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
        model.remove("clientes");
        model.put("clientes", clientes.getContent());

        // Ahora, si desde React por ejemplo quisiéramos leer este JSON, tendríamos que hacer referencia
        // al atributo clientes y ya podríamos iterar en este arreglo de objetos.

        return super.filterModel(model);
    }
}
