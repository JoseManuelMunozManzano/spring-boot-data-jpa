package com.jmunoz.springboot.app.view.xml;

import com.jmunoz.springboot.app.models.entity.Cliente;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

// Esta es nuestra clase wrapper
// Si no se le asigna un nombre, por defecto toma el nombre de la clase
@XmlRootElement(name = "clientes")
public class ClienteList {

    // Tenemos que indicar cuál es el atributo que va a ser un elemento XML por cada objeto cliente que tengamos
    @XmlElement(name = "cliente")
    public List<Cliente> clientes;

    // Importante que tenga el constructor vacío para que lo pueda manejar el Jaxb2Marshaller, el Bean encargado
    // de convertir
    public ClienteList() {
    }

    public ClienteList(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    // Solo hace falta el método get
    public List<Cliente> getClientes() {
        return clientes;
    }
}
