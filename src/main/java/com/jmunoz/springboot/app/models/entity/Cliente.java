package com.jmunoz.springboot.app.models.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

// Para indicar que una clase POJO con atributos getter y setter que están mapeados a una tabla es una entidad
// de JPA o Hibernate, se usa la anotación @Entity
// Automáticamente, una clase que está anotada con la anotación @Entity, los campos de su clase
// se van a mapear de forma automática sin tener que configurar nada. Por defecto mapea a un campo que se llama
// exactamente igual.
// Si el nombre de un atributo se llama distinto al de la table se usa @Column para customizarlo.
// En el ejemplo el nombre de la BD es nombre_cliente.
// También se puede indicar el length en la BD.
// Si acepta nulos (nullable) por defecto true.
// precision para trabajar con decimales.
// Si el campo es unique.
// Si el campo o atributo es actualizable (updatable) o insertable (insertable)
//
// @Table
// La configuración del nombre de la tabla es opcional.
// No se informa si queremos que nuestra clase Entity se llame exactamente igual que la tabla en la BD.
// Se informa el nombre de la tabla en minúsculas y en plural. Esta forma de nombrar la tabla se usa mucho en MySql.
//
// Una entidad tiene que implementar la interfaz Serializable siempre que necesitemos guardar el objeto cliente en la
// sesión http para que se pueda serializar, o bien si necesitamos convertir el objeto a bytes para transmitirlo
// a la memoria, a una BD o JSON o XML
@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Ojo, que sea de tipo clase Long, no del primitivo long, ya que así podemos trabajar con conversiones de tipos.
    // @Id indica que este atributo en la llave
    // @GeneratedValue indica la estrategia en que genera la llave la BD, el motor. En MySql por defecto
    // sería un autoincremental, de uno en uno..
    // H2, SqlServer, MySql usan IDENTITY
    // PostGreSql y ORACLE usa SEQUENCE
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String email;

    // En MySql los nombres compuestos se separan en MySql con guion bajo.
    // Solo para fechas se puede usar @Temporal. Indica el formato en que se guarda la fecha Java en la tabla de la BD.
    // Si sólo DATE, TIME o TIMESTAMP.
    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    private Date createAt;

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
}
