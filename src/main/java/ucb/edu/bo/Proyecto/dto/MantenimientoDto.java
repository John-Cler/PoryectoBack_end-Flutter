package ucb.edu.bo.Proyecto.dto;

import ucb.edu.bo.Proyecto.entity.AsignacionMantenimiento;
import ucb.edu.bo.Proyecto.entity.SolicitudMantenimiento;

import java.util.Date;

public class MantenimientoDto {

    private Integer id;
    private EquipoDto equipoDto;
    private Integer id_equipo;
    private SolicitudMantenimientoDto solicitudMantenimientoDto;
    private Integer id_solicitud_mantenimiento;
    private AsignacionMantenimientoDto asignacionMantenimientoDto;
    private Integer id_asignacion_mantenimiento;
    private String descripcion;
    private String observaciones;
    private String estado_equipo;
    private Date fecha_registro;

    public EquipoDto getEquipoDto() {
        return equipoDto;
    }

    public void setEquipoDto(EquipoDto equipoDto) {
        this.equipoDto = equipoDto;
    }

    public Integer getId_equipo() {
        return id_equipo;
    }

    public void setId_equipo(Integer id_equipo) {
        this.id_equipo = id_equipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SolicitudMantenimientoDto getSolicitudMantenimientoDto() {
        return solicitudMantenimientoDto;
    }

    public void setSolicitudMantenimientoDto(SolicitudMantenimientoDto solicitudMantenimientoDto) {
        this.solicitudMantenimientoDto = solicitudMantenimientoDto;
    }

    public AsignacionMantenimientoDto getAsignacionMantenimientoDto() {
        return asignacionMantenimientoDto;
    }

    public void setAsignacionMantenimientoDto(AsignacionMantenimientoDto asignacionMantenimientoDto) {
        this.asignacionMantenimientoDto = asignacionMantenimientoDto;
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

    public Integer getId_solicitud_mantenimiento() {
        return id_solicitud_mantenimiento;
    }

    public void setId_solicitud_mantenimiento(Integer id_solicitud_mantenimiento) {
        this.id_solicitud_mantenimiento = id_solicitud_mantenimiento;
    }

    public Integer getId_asignacion_mantenimiento() {
        return id_asignacion_mantenimiento;
    }

    public void setId_asignacion_mantenimiento(Integer id_asignacion_mantenimiento) {
        this.id_asignacion_mantenimiento = id_asignacion_mantenimiento;
    }
}
