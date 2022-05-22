package com.jmunoz.springboot.app.models.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

// No necesitamos relación con ItemFactura, ya que en nuestro proyecto, dado un Producto no vamos a requerir los
// itemFactura. Se mapean solo por el lado ItemFactura (relación unidireccional)
@Entity
@Table(name = "productos")
public class Producto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private Double precio;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_at")
    private Date createAt;

    // La fecha de creación se va a manejar de forma automática
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
