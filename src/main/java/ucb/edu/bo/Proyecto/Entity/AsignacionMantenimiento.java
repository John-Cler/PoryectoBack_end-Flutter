package ucb.edu.bo.Proyecto.Entity;


import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "asignacion_mantenimientos")
public class AsignacionMantenimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "id_solicitud_mantenimiento", referencedColumnName = "id")
    private SolicitudMantenimiento solicitud_mantenimiento;
    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;
    @Column(name = "estado")
    private String estado;
    @Temporal(TemporalType.DATE)
    private Date fecha_asignacion;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha_asignacion() {
        return fecha_asignacion;
    }

    public void setFecha_asignacion(Date fecha_asignacion) {
        this.fecha_asignacion = fecha_asignacion;
    }
}
