package com.jmunoz.springboot.app.models.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

// TODO: Añadir el producto
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

    public Long calcularImporte() {
        return cantidad.longValue();
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
