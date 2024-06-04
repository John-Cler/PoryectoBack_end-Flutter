package ucb.edu.bo.Proyecto.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "mantenimientos")
public class Mantenimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_equipo", referencedColumnName = "id")
    private Equipo equipo;
    @OneToOne
    @JoinColumn(name = "id_solicitud_mantenimiento", referencedColumnName = "id")
    private SolicitudMantenimiento solicitud_mantenimiento;
    @OneToOne
    @JoinColumn(name = "id_asignacion_mantenimiento", referencedColumnName = "id")
    private AsignacionMantenimiento asignacion_mantenimiento;
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    @Column(name = "estado_equipo")
    private String estado_equipo;
    @Temporal(TemporalType.DATE)
    private Date fecha_registro;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SolicitudMantenimiento getSolicitud_mantenimiento() {
        return solicitud_mantenimiento;
    }

    public void setSolicitud_mantenimiento(SolicitudMantenimiento solicitud_mantenimiento) {
        this.solicitud_mantenimiento = solicitud_mantenimiento;
    }

    public AsignacionMantenimiento getAsignacion_mantenimiento() {
        return asignacion_mantenimiento;
    }

    public void setAsignacion_mantenimiento(AsignacionMantenimiento asignacion_mantenimiento) {
        this.asignacion_mantenimiento = asignacion_mantenimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado_equipo() {
        return estado_equipo;
    }

    public void setEstado_equipo(String estado_equipo) {
        this.estado_equipo = estado_equipo;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
}
