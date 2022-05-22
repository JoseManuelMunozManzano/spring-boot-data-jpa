package com.jmunoz.springboot.app.models.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "facturas")
public class Factura implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private String observacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_at")
    private Date createAt;

    // Muchas facturas, un cliente
    //
    // La carga es perezosa, a medida que se van invocando los métodos. Es decir, cuando se hace un getCliente()
    // entonces ahí se hace la consulta del cliente de la factura
    // EAGER trae to-do de una vez
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    // La fecha de creación se va a manejar de forma automática
    // Con esto no es necesario tener la fecha en el formulario, ya que es una fecha interna
    // Justo antes de persistir la factura le asigna la fecha
    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
