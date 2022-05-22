package com.jmunoz.springboot.app.models.entity;

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

    // Muchos itemFactura, un producto (podría se también @OneToOne, pero lo importante es el ToOne, la relación
    // por el lado del ItemFactura)
    //
    // Por defecto, ya que estamos mapeando producto, va a crear el atributo producto_id en la tabla facturas_items
    // de forma automática, pero igual se puede especificar de forma explícita con @JoinColumn
    // Así relacionamos la tabla facturas_items con la tabla productos.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
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

    @Serial
    private static final long serialVersionUID = 1L;
}
