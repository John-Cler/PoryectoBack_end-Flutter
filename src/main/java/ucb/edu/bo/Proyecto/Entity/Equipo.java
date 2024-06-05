package ucb.edu.bo.Proyecto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@NamedQuery(name = "Equipo.findByCodigo", query = "select e from Equipo e where e.codigo=:codigo")
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "equipos")
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="codigo",unique = true)
    private String codigo;
    @Column(name="tipo")
    private String tipo;
    @ManyToOne
    @JoinColumn(name = "id_departamento",referencedColumnName = "id")
    private Departamento departamento;
    @ManyToOne
    @JoinColumn(name = "id_bloque",referencedColumnName = "id")
    private Bloque bloque;
    @Column(name="descripcion")
    private String descripcion;
    @Column(name="nro_activo")
    private String nro_activo;
    @Column(name="nro_serie")
    private String nro_serie;
    @Column(name="marca")
    private String marca;
    @Column(name="modelo")
    private String modelo;
    @Column(name="estado")
    private String estado;
    @OneToOne(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    private AsignacionEquipo asignacionEquipo;
    @JsonIgnore
    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EquipoParte> equipo_partes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNro_activo() {
        return nro_activo;
    }

    public void setNro_activo(String nro_activo) {
        this.nro_activo = nro_activo;
    }

    public String getNro_serie() {
        return nro_serie;
    }

    public void setNro_serie(String nro_serie) {
        this.nro_serie = nro_serie;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public AsignacionEquipo getAsignacionEquipo() {
        return asignacionEquipo;
    }

    public void setAsignacionEquipo(AsignacionEquipo asignacionEquipo) {
        this.asignacionEquipo = asignacionEquipo;
    }

    public List<EquipoParte> getEquipo_partes() {
        return equipo_partes;
    }

    public void setEquipo_partes(List<EquipoParte> equipo_partes) {
        this.equipo_partes = equipo_partes;
    }
}