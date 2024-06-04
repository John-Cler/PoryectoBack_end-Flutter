package ucb.edu.bo.Proyecto.dto;

import ucb.edu.bo.Proyecto.entity.Equipo;
import ucb.edu.bo.Proyecto.entity.User;
import java.util.Date;

public class SolicitudMantenimientoDto {
    private Integer id;
    private UserDto userDto;
    private Integer id_user;
    private EquipoDto equipoDto;
    private Integer id_equipo;
    private String motivo;
    private String estado;
    private Date fecha_registro;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

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
}
