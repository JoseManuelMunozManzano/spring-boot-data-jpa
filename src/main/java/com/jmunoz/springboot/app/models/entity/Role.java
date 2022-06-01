package com.jmunoz.springboot.app.models.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;

// Implementamos Serializable por si en algún momento lo queremos guardar en la sesión HTTP y pueda ser persistente
//   entre requests
// @Entity para indicar que es una clase de persistencia mapeada a la BD
// Se indica en @Table que los campos authority y user_id son únicos. Se indica aquí porque es a nivel de tabla,
//   no de atributos de la clase. Es para cuando se genere de forma automática la tabla a partir de la clase Entity
//   pueda crear ese constraint.
// @UniqueConstraint es un arreglo y podemos tener más de un constraint separados por comas.
@Entity
@Table(name = "authorities", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "authority"})})
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Serial
    private static final long serialVersionUID = 8735859523196973600L;
}
