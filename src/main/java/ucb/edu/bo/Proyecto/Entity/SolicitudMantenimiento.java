package ucb.edu.bo.Proyecto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "solicitud_mantenimientos")
public class SolicitudMantenimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="codigo",unique = true)
    private String codigo;
    @ManyToOne
    @JoinColumn(name = "id_user",referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "id_equipo",referencedColumnName = "id")
    private Equipo equipo;
    @Column(name="motivo")
    private String motivo;
    @Column(name="estado")
    private String estado;
    @Temporal(TemporalType.DATE)
    private Date fecha_registro;

    @JsonIgnore
    @OneToOne(mappedBy = "solicitud_mantenimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private AsignacionMantenimiento asignacionMantenimiento;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public AsignacionMantenimiento getAsignacionMantenimiento() {
        return asignacionMantenimiento;
    }

    public void setAsignacionMantenimiento(AsignacionMantenimiento asignacionMantenimiento) {
        this.asignacionMantenimiento = asignacionMantenimiento;
    }
}
