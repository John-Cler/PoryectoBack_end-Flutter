package ucb.edu.bo.Proyecto.dto;

import ucb.edu.bo.Proyecto.entity.AsignacionEquipo;
import ucb.edu.bo.Proyecto.entity.Bloque;
import ucb.edu.bo.Proyecto.entity.Departamento;
import ucb.edu.bo.Proyecto.entity.User;

import java.util.List;

public class AdministrativoDto {
    private Integer id;
    private Integer id_user;
    private DepartamentoDto departamentoDto;
    private Integer id_departamento;
    private BloqueDto bloqueDto;
    private Integer id_bloque;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DepartamentoDto getDepartamentoDto() {
        return departamentoDto;
    }

    public void setDepartamentoDto(DepartamentoDto departamentoDto) {
        this.departamentoDto = departamentoDto;
    }

    public BloqueDto getBloqueDto() {
        return bloqueDto;
    }

    public void setBloqueDto(BloqueDto bloqueDto) {
        this.bloqueDto = bloqueDto;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public Integer getId_departamento() {
        return id_departamento;
    }

    public void setId_departamento(Integer id_departamento) {
        this.id_departamento = id_departamento;
    }

    public Integer getId_bloque() {
        return id_bloque;
    }

    public void setId_bloque(Integer id_bloque) {
        this.id_bloque = id_bloque;
    }
}
