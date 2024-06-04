package ucb.edu.bo.Proyecto.dto;
import ucb.edu.bo.Proyecto.entity.Administrativo;
import ucb.edu.bo.Proyecto.entity.Equipo;
import ucb.edu.bo.Proyecto.entity.User;
//import jakarta.persistence.*;

public class AsignacionEquipoDto {
    private Integer id;
    private EquipoDto equipoDto;
    private Integer id_equipo;
    private Integer id_administrativo;
    private Integer id_user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getId_administrativo() {
        return id_administrativo;
    }

    public void setId_administrativo(Integer id_administrativo) {
        this.id_administrativo = id_administrativo;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }
}
