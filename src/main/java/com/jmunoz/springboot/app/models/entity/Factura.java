package com.jmunoz.springboot.app.models.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    // Para obtener todas las líneas de la factura dada su factura
    //
    // Una factura puede tener muchas líneas de factura
    //
    // IMPORTANTÍSIMO
    // Con @JoinColumn, como la relación es unidireccional, se indica cuál es la llave foránea.
    // Por tanto, en la tabla facturas_items vamos a tener un campo llamado factura_id
    //
    // Se añade orphanRemoval a true que es opcional y sirve para eliminar registros huérfanos que no están
    // asociados a ningún cliente
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "factura_id")
    private List<ItemFactura> items;

    public Factura() {
        this.items = new ArrayList<>();
    }

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

    public List<ItemFactura> getItems() {
        return items;
    }

    public void setItems(List<ItemFactura> items) {
        this.items = items;
    }

    public void addItemFactura(ItemFactura item) {
        this.items.add(item);
    }

    public Double getTottal() {
        Double total = 0.0;

        for (ItemFactura item : items) {
            total += item.calcularImporte();
        }

        return total;
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
