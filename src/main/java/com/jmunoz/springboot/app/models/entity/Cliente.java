package com.jmunoz.springboot.app.models.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String nombre;

    @NotEmpty
    private String apellido;

    @NotEmpty
    @Email
    private String email;

    @NotNull
    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;

    // Un cliente puede tener muchas facturas
    //
    // Aquí indicar LAZY es todavía más importante para evitar que se traiga todas las facturas de cada cliente (EAGER)
    //
    // CascadeType.ALL indica que todas las operaciones delete y persist se van a realizar en cadena. Por ejemplo,
    // cuando al Cliente se le asignen varias facturas y el cliente se persiste con el método persist, automáticamente
    // va a persistir también a sus elementos hijos (facturas)
    // O si el cliente se elimina, también se van a eliminar automáticamente todas sus facturas.
    //
    // Con mappedBy se hace bidireccional. Se indica que cliente va a tener una lista de facturas y factura va a
    // tener un cliente. El nombre es el que aparece en la clase Factura. De forma automática se crea la clave
    // foránea cliente_id en la tabla facturas para relacionar ambas tablas.
    //
    // Se añade orphanRemoval a true que es opcional y sirve para eliminar registros huérfanos que no están
    // asociados a ningún cliente
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Factura> facturas;

    private String foto;

    public Cliente() {
        facturas = new ArrayList<>();
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    public void addFactura(Factura factura) {
        facturas.add(factura);
    }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}
