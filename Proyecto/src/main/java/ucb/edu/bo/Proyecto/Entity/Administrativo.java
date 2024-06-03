//package com.backendInventarios.persistence.entities;
package ucb.edu.bo.Proyecto.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "administrativos")
public class Administrativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "id_departamento",referencedColumnName = "id")
    private Departamento departamento;
    @ManyToOne
    @JoinColumn(name = "id_bloque",referencedColumnName = "id")
    private Bloque bloque;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "administrativo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AsignacionEquipo> asignacion_equipos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Bloque getBloque() {
        return bloque;
    }

    public void setBloque(Bloque bloque) {
        this.bloque = bloque;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<AsignacionEquipo> getAsignacion_equipos() {
        return asignacion_equipos;
    }

    public void setAsignacion_equipos(List<AsignacionEquipo> asignacion_equipos) {
        this.asignacion_equipos = asignacion_equipos;
    }
}
