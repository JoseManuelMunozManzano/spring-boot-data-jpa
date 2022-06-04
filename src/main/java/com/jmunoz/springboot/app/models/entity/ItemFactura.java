package com.jmunoz.springboot.app.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

// No tiene relación con factura porque para este proyecto no la necesita. En ningún momento vamos a utilizar
// un ItemFactura para obtener la factura
// Relación unidireccional de Factura con ItemFactura
@Entity
@Table(name = "facturas_items")
public class ItemFactura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;

    // Otro problema que se da en la conversión a JSON esta relacionado con esta carga perezosa.
    // Por qué? Porque hay atributos que no se pueden serializar de forma correcta.
    // Hay una relación de Factura con los ItemFactura y de los Item con Producto.
    //
    // En concreto el problema viene del atributo de Producto llamado handler.
    // En teoría Producto no tiene ningún atributo que se llame handler, PERO SI QUE LO TIENE,
    // Por qué? Porque no es el objeto producto original, es un proxy, ya que lo estamos cargando de manera perezosa
    // a través del LAZY. Cada vez que se llama al atributo producto a través del método get, en ese momento es
    // cuando se realiza la consulta y se obtiene el producto, pero se mantiene como un proxy, no como un tipo
    // original. Es un proxy que hereda de producto, y tiene algunos atributos extra como por ejemplo handler,
    // hibernateLazyInitializer...
    //
    // La segunda solución, mucho mejor, es seguir dejando FetchType.LAZY y agregar una nueva anotación
    // llamada @JsonIgnoreProperties
    // Sirve para que se ignoren algunos atributos del objeto Producto.
    // Esta anotación puede ir sobre el atributo o sobre la clase Producto.
    // Hay que indicar un arreglo de Strings con los nombres de los atributos que queremos ignorar.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Producto producto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double calcularImporte() {
        return cantidad.doubleValue() * producto.getPrecio();
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
