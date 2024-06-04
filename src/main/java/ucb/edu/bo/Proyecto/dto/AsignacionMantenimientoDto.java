package ucb.edu.bo.Proyecto.dto;

import ucb.edu.bo.Proyecto.entity.SolicitudMantenimiento;
import ucb.edu.bo.Proyecto.entity.User;
import jakarta.persistence.*;

import java.util.Date;

public class AsignacionMantenimientoDto {
    private Integer id;
    private SolicitudMantenimientoDto solicitudMantenimientoDto;
    private Integer id_solicitud_mantenimiento;
    private UserDto userDto;
    private Integer id_user;
    private String estado;
    private Date fecha_asignacion;

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

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
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

    public Integer getId_solicitud_mantenimiento() {
        return id_solicitud_mantenimiento;
    }

    public void setId_solicitud_mantenimiento(Integer id_solicitud_mantenimiento) {
        this.id_solicitud_mantenimiento = id_solicitud_mantenimiento;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }
}
